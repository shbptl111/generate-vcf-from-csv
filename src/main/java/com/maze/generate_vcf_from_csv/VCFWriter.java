package com.maze.generate_vcf_from_csv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import org.springframework.stereotype.Component;

@Component
public class VCFWriter {

	public void cleanData(LinkedList<String[]> records, int recordSize) {

		String fullName = null;
		String streetAddress = null;
		String city = null;
		String state = null;
		String country = null;
		String postalCode = null;
		String phoneNumber = null;
		String phoneNumber2 = "";
		String emailAddress = null;
		String emailAddress2 = "";

		LinkedList<String[]> cleanedRecords = new LinkedList<String[]>();

		for (String[] record : records) {
			
			phoneNumber2 = "";
			emailAddress2 = "";
			
			String[] contact = new String[10];

			fullName = record[0].trim().toUpperCase();
			streetAddress = record[1].trim().toUpperCase();
			city = record[2].trim().toUpperCase();
			state = record[3].trim().toUpperCase();
			country = record[4].trim().toUpperCase();
			postalCode = record[5].trim();

			if (record[6].trim().length() <= 20) {
				phoneNumber = record[6].trim().replaceAll("[^0-9]", "").trim();
			} else if (record[6].trim().length() > 20) {
				phoneNumber = record[6].trim();
			}

			emailAddress = record[7].trim();

			if (country.equals("USA")) {
				postalCode = postalCode.trim().replaceAll("[^0-9]", "").trim();

				if (postalCode.length() == 3) {
					postalCode = "00" + postalCode;
				}

				if (postalCode.length() == 4) {
					postalCode = "0" + postalCode;
				}
			}

			if (country.contentEquals("CAN")) {
				postalCode = postalCode.toUpperCase();
				if (postalCode.length() == 6) {
					postalCode = postalCode.substring(0, 3) + " " + postalCode.substring(3, 6);
				}

			}

			if (phoneNumber.length() == 11) {
				phoneNumber = phoneNumber.substring(1, 11);
			}

			if (phoneNumber.length() > 11) {
				String[] phoneNumbers = phoneNumber.split("/");
				phoneNumber = phoneNumbers[0].trim().replaceAll("[^0-9]", "").trim();
				phoneNumber2 = phoneNumbers[1].trim().replaceAll("[^0-9]", "").trim();

				if (phoneNumber.length() == 11) {
					phoneNumber = phoneNumber.substring(1, 11);
				}

				if (phoneNumber2.length() == 11) {
					phoneNumber2 = phoneNumber2.substring(1, 11);
				}
			}

			if (emailAddress.contains("/")) {
				String[] emailAddresses = emailAddress.split("/");
				emailAddress = emailAddresses[0].trim();
				emailAddress2 = emailAddresses[1].trim();
			}

			contact[0] = fullName;
			contact[1] = streetAddress;
			contact[2] = city;
			contact[3] = state;
			contact[4] = country;
			contact[5] = postalCode;
			contact[6] = "+1-" + phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 6) + "-"
					+ phoneNumber.substring(6, 10);

			if (phoneNumber2.length() > 0) {
				contact[7] = "+1-" + phoneNumber2.substring(0, 3) + "-" + phoneNumber2.substring(3, 6) + "-"
						+ phoneNumber2.substring(6, 10);
			} else {
				contact[7] = "";
			}

			contact[8] = emailAddress;
			contact[9] = emailAddress2;

			cleanedRecords.add(contact);

		}

		writeVCF(cleanedRecords);

	}

	public static void writeVCF(LinkedList<String[]> records) {

		int fullName = 0, streetAddress = 1, city = 2, state = 3, country = 4, postalCode = 5, phoneNumber = 6,
				phoneNumber2 = 7, emailAddress = 8, emailAddress2 = 9;
		String totalContent = "";
		int count = 0;
		FileOutputStream fileOut = null;
		for (String[] record : records) {

			String content = "BEGIN:VCARD\n" + "VERSION:3.0\n" + "FN:" + record[fullName] + "\n" + "ADR;TYPE=PARCEL:;;"
					+ record[streetAddress] + ";" + record[city] + ";" + record[state] + ";" + record[postalCode] + ";"
					+ record[country] + "\n" + "TEL;TYPE=HOME;VOICE:" + record[phoneNumber] + "\n" + "TEL;TYPE=WORK;VOICE:"
					+ record[phoneNumber2] + "\n" + "EMAIL;TYPE=INTERNET:" + record[emailAddress] + "\n"
					+ "EMAIL;TYPE=INTERNET:" + record[emailAddress2] + "\n" + "END:VCARD";

			count++;

			if (count != records.size()) {
				totalContent = totalContent + content + System.lineSeparator();
			} else if (count == records.size()) {
				totalContent += content;
			}
		}

		String newTotalContent = totalContent.replaceAll("\n", System.lineSeparator());

		try {
			System.out.println("Writing the VCF file");
			fileOut = new FileOutputStream(new File("C:\\Users\\admin\\Desktop\\Total Sales Sheet Contacts.vcf"));
			fileOut.write(newTotalContent.getBytes());
			fileOut.flush();
			fileOut.close();
			System.out.println("Writing complete");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
