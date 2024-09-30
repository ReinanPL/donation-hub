package com.compass.DAO;

import com.compass.model.Donation;
import com.compass.model.enums.ItemType;

import java.util.List;

public interface DonationDAO extends GenericDAO<Donation, Long> {

    public List<Donation> getDonationByIdDistributionCenter(long id);
    public List<Donation> getDonationByItemType(ItemType itemType);

}
