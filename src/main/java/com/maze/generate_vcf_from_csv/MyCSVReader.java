package com.maze.generate_vcf_from_csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

@Component
public class MyCSVReader {

	@Autowired
	VCFWriter vcfWriter;

	public void readCSVFile(String filePath) {
		LinkedList<String[]> records = null;
		File file = new File(filePath);
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		CSVReader csvReader = new CSVReaderBuilder(fileReader).withSkipLines(1).build();
		try {
			System.out.println("Reading the CSV file");
			records = (LinkedList<String[]>) csvReader.readAll();
			csvReader.close();
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Number of records found in the CSV file: " + records.size());

		vcfWriter.cleanData(records, records.size());
	}
}
