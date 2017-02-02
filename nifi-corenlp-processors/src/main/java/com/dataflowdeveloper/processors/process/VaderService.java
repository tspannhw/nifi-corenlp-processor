package com.dataflowdeveloper.processors.process;

import java.io.IOException;
import java.util.HashMap;

//import com.vader.SentimentAnalyzer;

/**
 * 
 * @author tspann
 *
 */
public class VaderService {

	/**
	 * 
	 * @param message
	 */
	public String vader(String message) {

//		SentimentAnalyzer sentimentAnalyzer = null;
//		try {
//			sentimentAnalyzer = new SentimentAnalyzer(message);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		HashMap<String, Float> map = null;
//		try {
//			sentimentAnalyzer.analyse();
//			map = sentimentAnalyzer.getPolarity();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		try {
//			System.out.println("Sentiment Vader:" + map.getOrDefault("negative", -1f));
//			System.out.println("Sentiment Vader:" + map.getOrDefault("neutral", -1f).toString());
//			System.out.println("map:" + map.getOrDefault("positive", -1f).toString());
//			
//			map.forEach((k,v)->{
//				System.out.println("Item : " + k + " Count : " + v);
//			});
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		return "positive";

	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		VaderService service = new VaderService();
		service.vader("This is the worst class ever");
		service.vader("This is the best Java ever");

	}
}
