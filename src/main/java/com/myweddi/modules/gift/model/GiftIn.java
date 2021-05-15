package com.myweddi.modules.gift.model;

import com.myweddi.modules.gift.GiftType;
import com.myweddi.modules.gift.GiftWrapper;

import java.util.HashMap;
import java.util.Map;

public class GiftIn {

    private boolean POTTEDFLOWERS;
    private boolean WINE;
    private boolean BOOKS;
    private boolean FOODER;
    private boolean LOTTERY;
    private boolean CHARITY;

    private boolean GIFTS;
    private boolean MONEY;

    private String giftInfo;

    public Map<GiftType, Boolean> giftsToMap(){
        Map<GiftType, Boolean> gifts = new HashMap<>();
        gifts.put(GiftType.POTTEDFLOWERS, this.POTTEDFLOWERS);
        gifts.put(GiftType.WINE, this.WINE);
        gifts.put(GiftType.BOOKS, this.BOOKS);
        gifts.put(GiftType.FOODER, this.FOODER);
        gifts.put(GiftType.LOTTERY, this.LOTTERY);
        gifts.put(GiftType.CHARITY, this.CHARITY);
        gifts.put(GiftType.GIFTS, this.GIFTS);
        gifts.put(GiftType.MONEY, this.MONEY);
        return gifts;
    }

    public void mapToGift(Map<GiftType, Boolean> map){
        this.POTTEDFLOWERS = map.get(GiftType.POTTEDFLOWERS);
        this.WINE = map.get(GiftType.WINE);
        this.BOOKS = map.get(GiftType.BOOKS);
        this.FOODER = map.get(GiftType.FOODER);
        this.LOTTERY = map.get(GiftType.LOTTERY);
        this.CHARITY = map.get(GiftType.CHARITY);
        this.GIFTS = map.get(GiftType.GIFTS);
        this.MONEY = map.get(GiftType.MONEY);
    }



    public GiftIn() {
    }

    public GiftIn(GiftWrapper wrapper) {
        this.giftInfo = wrapper.getGiftInfo();
        mapToGift(wrapper.getSelectedGift());
    }

    public Boolean getPOTTEDFLOWERS() {
        return POTTEDFLOWERS;
    }

    public boolean isPOTTEDFLOWERS() {
        return POTTEDFLOWERS;
    }

    public void setPOTTEDFLOWERS(boolean POTTEDFLOWERS) {
        this.POTTEDFLOWERS = POTTEDFLOWERS;
    }

    public boolean isWINE() {
        return WINE;
    }

    public void setWINE(boolean WINE) {
        this.WINE = WINE;
    }

    public boolean isBOOKS() {
        return BOOKS;
    }

    public void setBOOKS(boolean BOOKS) {
        this.BOOKS = BOOKS;
    }

    public boolean isFOODER() {
        return FOODER;
    }

    public void setFOODER(boolean FOODER) {
        this.FOODER = FOODER;
    }

    public boolean isLOTTERY() {
        return LOTTERY;
    }

    public void setLOTTERY(boolean LOTTERY) {
        this.LOTTERY = LOTTERY;
    }

    public boolean isCHARITY() {
        return CHARITY;
    }

    public void setCHARITY(boolean CHARITY) {
        this.CHARITY = CHARITY;
    }

    public boolean isGIFTS() {
        return GIFTS;
    }

    public void setGIFTS(boolean GIFTS) {
        this.GIFTS = GIFTS;
    }

    public boolean isMONEY() {
        return MONEY;
    }

    public void setMONEY(boolean MONEY) {
        this.MONEY = MONEY;
    }

    public String getGiftInfo() {
        return giftInfo;
    }

    public void setGiftInfo(String giftInfo) {
        this.giftInfo = giftInfo;
    }

    @Override
    public String toString() {
        return "GiftIn{" +
                "POTTEDFLOWERS=" + POTTEDFLOWERS +
                ", WINE=" + WINE +
                ", BOOKS=" + BOOKS +
                ", FOODER=" + FOODER +
                ", LOTTERY=" + LOTTERY +
                ", CHARITY=" + CHARITY +
                ", GIFTS=" + GIFTS +
                ", MONEY=" + MONEY +
                ", giftInfo='" + giftInfo + '\'' +
                '}';
    }
}
