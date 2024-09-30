package com.compass.util;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.compass.DAO.DistributionCenterDAO;
import com.compass.DAO.DonationDAO;
import com.compass.DAO.impl.DistributionCenterDAOImpl;
import com.compass.DAO.impl.DonationDAOImpl;
import com.compass.Exception.CsvReaderException;
import com.compass.model.DistributionCenter;
import com.compass.model.Donation;
import com.compass.model.Lot;
import com.compass.model.enums.GenreCloth;
import com.compass.model.enums.ItemType;
import com.compass.model.enums.SizeCloth;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import jakarta.persistence.EntityManager;

public class CsvUtil {

	EntityManager em = EntityManagerFactorySingleton.getInitDb();

	private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private final DonationDAO donationDAO = new DonationDAOImpl(em);

	private final DistributionCenterDAO centerDAO = new DistributionCenterDAOImpl(em);

    public void importCsv(){

        try (CSVReader csvReader = new CSVReader(new FileReader(
				"C:\\Users\\Reinan\\Documents\\Projetos\\donation-hub\\src\\main\\resources\\META-INF\\donation.csv"))) {
			String[] headers = csvReader.readNext();
			String[] nextRecord;

			while ((nextRecord = csvReader.readNext()) != null) {
				var donation = new Donation();
				var lot = new Lot();
				Long distributionCenterId = null;

				for (int i = 0; i < headers.length; i++) {
					var header = headers[i];
					var value = nextRecord[i];

					switch (header) {
					case "distribution_center_id": 
						distributionCenterId = Long.parseLong(value);
						break;
					case "name":
						lot.setName(value);
						break;
					case "item_type":
						lot.setItemType(ItemType.valueOf(value));
						break;
					case "description": 
						lot.setDescription(value);
						break;
					case "quantity":
						lot.setQuantity(Integer.parseInt(value));
						break;
					case "unit_of_measurement":
						lot.setUnitOfMeasurement(value);
						break;
					case "validity":
						if (lot.getItemType() == ItemType.FOOD) {
							var expirationDate = LocalDate.parse(value, dtf);
							lot.setValidity(expirationDate);
						}
						break;
					case "genre":
						if (lot.getItemType() == ItemType.CLOTHING) {
							lot.setGenre(GenreCloth.valueOf(value));
						}
						break;
					case "size":
						if (lot.getItemType() == ItemType.CLOTHING) {
							lot.setSize(SizeCloth.valueOf(value));
						}
						break;

					}
				}

				var distributionCenter = centerDAO.find(distributionCenterId);
				donation.setDistributionCenter(distributionCenter);
				donation.setLot(lot);

				donationDAO.create(donation);
			}
			System.out.println("\n=== Doações registradas com sucesso! ===\n");
		} catch (IOException | CsvValidationException e) {
            throw new CsvReaderException("Erro ao ler o arquivo CSV: " + e.getMessage()); 
        }
    }

    }
    

