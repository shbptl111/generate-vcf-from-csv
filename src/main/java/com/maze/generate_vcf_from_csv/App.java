package com.maze.generate_vcf_from_csv;

import java.util.Scanner;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
	public static void main(String[] args) {

		String filePath = null;
		Scanner scanner = null;
		AnnotationConfigApplicationContext context = null;

		try {
			context = new AnnotationConfigApplicationContext();
			context.scan("com.maze.generate_vcf_from_csv");
			context.refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}

		MyCSVReader myCSVReader = (MyCSVReader) context.getBean(MyCSVReader.class);

		System.out.print("Select the CSV file: ");
		scanner = new Scanner(System.in);
		filePath = scanner.nextLine();
		try {
			if (scanner != null)
				scanner.close();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		myCSVReader.readCSVFile(filePath);

		context.close();

	}
}
