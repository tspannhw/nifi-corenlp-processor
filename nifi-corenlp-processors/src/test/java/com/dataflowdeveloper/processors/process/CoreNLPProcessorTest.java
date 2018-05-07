/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dataflowdeveloper.processors.process;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.processor.util.StandardValidators;
import org.apache.nifi.util.MockFlowFile;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.Before;
import org.junit.Test;

public class CoreNLPProcessorTest {

	private TestRunner testRunner;

	public static final String ATTRIBUTE_INPUT_NAME = "sentence";
	
    public static final PropertyDescriptor MY_PROPERTY = new PropertyDescriptor
            .Builder().name(ATTRIBUTE_INPUT_NAME)
            .description("A sentence to analyze for sentiment, such as a Tweet.")
            .required(true)
            .expressionLanguageSupported(true)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();
    
	@Before
	public void init() {
		testRunner = TestRunners.newTestRunner(CoreNLPProcessor.class);
	}

	@Test
	public void testProcessor() {
		
		testRunner.setProperty(MY_PROPERTY, "This is the worst unit test of sentiment analysis ever, just horrible. ");
		
		try {
			testRunner.enqueue(new FileInputStream(new File("src/test/resources/test.csv")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		testRunner.setValidateExpressionUsage(false);
		testRunner.run();
		testRunner.assertValid();
		List<MockFlowFile> successFiles = testRunner.getFlowFilesForRelationship(CoreNLPProcessor.REL_SUCCESS);

		for (MockFlowFile mockFile : successFiles) {
			try {
				System.out.println("FILE:" + new String(mockFile.toByteArray(), "UTF-8"));
				System.out.println("Attribute: " + mockFile.getAttribute(CoreNLPProcessor.ATTRIBUTE_OUTPUT_NAME));
				
				assertNotNull(  mockFile.getAttribute(CoreNLPProcessor.ATTRIBUTE_OUTPUT_NAME) );
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testProcessorHappy() {
		
		testRunner.setProperty(MY_PROPERTY, "This is best use of Apache NiFi that I have ever seen, good job. ");
		
		try {
			testRunner.enqueue(new FileInputStream(new File("src/test/resources/test.csv")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		testRunner.setValidateExpressionUsage(false);
		testRunner.run();
		testRunner.assertValid();
		List<MockFlowFile> successFiles = testRunner.getFlowFilesForRelationship(CoreNLPProcessor.REL_SUCCESS);

		for (MockFlowFile mockFile : successFiles) {
			try {
				System.out.println("FILE:" + new String(mockFile.toByteArray(), "UTF-8"));
				System.out.println("Attribute: " + mockFile.getAttribute(CoreNLPProcessor.ATTRIBUTE_OUTPUT_NAME));
				
				assertNotNull(  mockFile.getAttribute(CoreNLPProcessor.ATTRIBUTE_OUTPUT_NAME) );
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test
	public void testProcessorNeutral() {
		
		testRunner.setProperty(MY_PROPERTY, "Cats are black.");
		
		try {
			testRunner.enqueue(new FileInputStream(new File("src/test/resources/test.csv")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		testRunner.setValidateExpressionUsage(false);
		testRunner.run();
		testRunner.assertValid();
		List<MockFlowFile> successFiles = testRunner.getFlowFilesForRelationship(CoreNLPProcessor.REL_SUCCESS);

		for (MockFlowFile mockFile : successFiles) {
			try {
				System.out.println("FILE:" + new String(mockFile.toByteArray(), "UTF-8"));
				System.out.println("Attribute: " + mockFile.getAttribute(CoreNLPProcessor.ATTRIBUTE_OUTPUT_NAME));
				
				assertNotNull(  mockFile.getAttribute(CoreNLPProcessor.ATTRIBUTE_OUTPUT_NAME) );
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
