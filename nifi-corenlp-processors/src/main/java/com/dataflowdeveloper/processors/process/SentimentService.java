/**
 * 
 */
package com.dataflowdeveloper.processors.process;

import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

/**
 * @author tspann
 *
 */
public class SentimentService {

	private static final String DEFAULT_VALUE = "Neutral";

	/**
	 * get stanford coreNLP sentiment analysis of sentence sent
	 * 
	 * @param sentence
	 * @return String of sentiment
	 */
	public String getSentimentNew(String sentence) {
		if (sentence == null) {
			return DEFAULT_VALUE;
		}
		String output = DEFAULT_VALUE;

		if (sentence != null) {
			try {
				Properties props = new Properties();
				props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
				props.setProperty("parse.binaryTrees", "true");
				props.setProperty("enforceRequirements", "false");
				props.setProperty("debug", "false");

				StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
				if (sentence != null && sentence.length() > 0) {
					Annotation annotation = pipeline.process(sentence);
					for (CoreMap sentenceStructure : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
						output = sentenceStructure.get(SentimentCoreAnnotations.SentimentClass.class);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return DEFAULT_VALUE;
			}
		}

		return output;
	}

	/**
	 * get stanford coreNLP sentiment analysis of sentence sent
	 * 
	 * @param sentence
	 * @return String of sentiment
	 */
	public String getSentiment(String sentence) {
		if (sentence == null) {
			return "";
		}
		String output = "";

		if (sentence != null) {
			try {
				Properties props = new Properties();
				props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
				props.setProperty("parse.binaryTrees", "true");
				props.setProperty("enforceRequirements", "false");
				props.setProperty("debug", "false");

				StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
				int mainSentiment = 0;
				if (sentence != null && sentence.length() > 0) {
					int longest = 0;
					Annotation annotation = pipeline.process(sentence);
					for (CoreMap sentenceStructure : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {

						String descr = sentenceStructure.get(SentimentCoreAnnotations.SentimentClass.class);
						System.out.println(descr);

						Tree tree = sentenceStructure.get(SentimentAnnotatedTree.class);
						int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
						String partText = sentence.toString();
						if (partText.length() > longest) {
							mainSentiment = sentiment;
							longest = partText.length();
						}
					}
				}

				String sentimentString = null;

				if (mainSentiment == 2 || mainSentiment > 4 || mainSentiment < 0) {
					sentimentString = "NEUTRAL";
				} else if (mainSentiment == 0 || mainSentiment == 1) {
					sentimentString = "NEGATIVE";
				} else {
					sentimentString = "POSITIVE";
				}

				output = sentimentString;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return output;
	}

	/**
	 * tester
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		if (args == null || args.length <= 0) {
			System.out.println("No Data");
			return;
		}

		long start_time = System.currentTimeMillis();
		SentimentService service = new SentimentService();

		for (int j = 0; j < args.length; j++) {
			System.out.println("Input:  " + args[j]);

			System.out.println("HAPPY SENT:" + service.getSentiment("This is a happy event that happened"));
			long end_time = System.currentTimeMillis();
			long difference = end_time - start_time;
			long seconds = (end_time - start_time) / 1000;
			System.out.println("Runtime:" + difference + " seconds " + seconds);
			System.out
					.println("SAD SENT:" + service.getSentiment("This is a very bad thing that happened and I am sad"));
		}
	}

}
