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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.nifi.annotation.behavior.ReadsAttribute;
import org.apache.nifi.annotation.behavior.ReadsAttributes;
import org.apache.nifi.annotation.behavior.WritesAttribute;
import org.apache.nifi.annotation.behavior.WritesAttributes;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.SeeAlso;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.annotation.lifecycle.OnScheduled;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.ProcessorInitializationContext;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.util.StandardValidators;

@Tags({ "corenlpprocessor" })
@CapabilityDescription("Run Stanford CoreNLP Sentiment Analysis")
@SeeAlso({})
@ReadsAttributes({ @ReadsAttribute(attribute = "sentence", description = "sentence to analyze") })
@WritesAttributes({
		@WritesAttribute(attribute = "sentiment", description = "Stanford CoreNLP sentiment analysis of that sentence.") })
public class CoreNLPProcessor extends AbstractProcessor {

	public static final String ATTRIBUTE_OUTPUT_NAME = "sentiment";
	public static final String ATTRIBUTE_INPUT_NAME = "sentence";
	public static final String PROPERTY_NAME_EXTRA = "Extra Resources";

	public static final PropertyDescriptor MY_PROPERTY = new PropertyDescriptor.Builder().name(ATTRIBUTE_INPUT_NAME)
			.description("A sentence to parse, such as a Tweet.").required(true).expressionLanguageSupported(true)
			.addValidator(StandardValidators.NON_EMPTY_VALIDATOR).build();

	public static final Relationship REL_SUCCESS = new Relationship.Builder().name("success")
			.description("Successfully determine sentiment.").build();

	public static final Relationship REL_FAILURE = new Relationship.Builder().name("failure")
			.description("Failed to determine sentiment.").build();

	private List<PropertyDescriptor> descriptors;

	private Set<Relationship> relationships;

	private SentimentService service;

	@Override
	protected void init(final ProcessorInitializationContext context) {
		final List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
		descriptors.add(MY_PROPERTY);
		this.descriptors = Collections.unmodifiableList(descriptors);

		final Set<Relationship> relationships = new HashSet<Relationship>();
		relationships.add(REL_SUCCESS);
		relationships.add(REL_FAILURE);
		this.relationships = Collections.unmodifiableSet(relationships);
	}

	@Override
	public Set<Relationship> getRelationships() {
		return this.relationships;
	}

	@Override
	public final List<PropertyDescriptor> getSupportedPropertyDescriptors() {
		return descriptors;
	}

	/**
	 * initialize sentiment service
	 */
	private void initService() {
		service = new SentimentService();
	}

	@OnScheduled
	public void onScheduled(final ProcessContext context) {
		initService();
		return;
	}

	@Override
	public void onTrigger(final ProcessContext context, final ProcessSession session) throws ProcessException {
		FlowFile flowFile = session.get();
		if (flowFile == null) {
			flowFile = session.create();
		}
		if (service == null) {
			initService();
		}
		try {
			flowFile.getAttributes();

			String sentence = flowFile.getAttribute(ATTRIBUTE_INPUT_NAME);
			String sentence2 = context.getProperty(ATTRIBUTE_INPUT_NAME).evaluateAttributeExpressions(flowFile)
					.getValue();

			if (sentence == null) {
				sentence = sentence2;
			}
			if (sentence == null) {
				return;
			}

			String value = service.getSentimentNew(sentence);

			if (value == null) {
				return;
			}

			flowFile = session.putAttribute(flowFile, ATTRIBUTE_OUTPUT_NAME, value);

			session.transfer(flowFile, REL_SUCCESS);
			session.commit();
		} catch (final Throwable t) {
			getLogger().error("Unable to process Sentiment Processor file " + t.getLocalizedMessage());
			getLogger().error("{} failed to process due to {}; rolling back session", new Object[] { this, t });
			throw t;
		}
	}
}