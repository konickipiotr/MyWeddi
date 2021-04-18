package com.myweddi.modules.gift;

import com.myweddi.modules.gift.model.Gift;

import java.util.List;
import java.util.Map;

public class GiftWrapper {

    private Long weddingid;
    private Map<GiftType, Boolean> selectedGift;
    private String giftInfo;
    private List<Gift> gifts;
    private boolean reservationImpossible;

    public GiftWrapper() {
    }

    public GiftWrapper(Long weddingid, Map<GiftType, Boolean> selectedGift, String giftInfo, List<Gift> gifts) {
        this.weddingid = weddingid;
        this.selectedGift = selectedGift;
        this.giftInfo = giftInfo;
        this.gifts = gifts;
    }


    public Long getWeddingid() {
        return weddingid;
    }

    public void setWeddingid(Long weddingid) {
        this.weddingid = weddingid;
    }

    public Map<GiftType, Boolean> getSelectedGift() {
        return selectedGift;
    }

    public void setSelectedGift(Map<GiftType, Boolean> selectedGift) {
        this.selectedGift = selectedGift;
    }

    public String getGiftInfo() {
        return giftInfo;
    }

    public void setGiftInfo(String giftInfo) {
        this.giftInfo = giftInfo;
    }

    public List<Gift> getGifts() {
        return gifts;
    }

    public void setGifts(List<Gift> gifts) {
        this.gifts = gifts;
    }

    public boolean isReservationImpossible() {
        return reservationImpossible;
    }

    public void setReservationImpossible(boolean reservationImpossible) {
        this.reservationImpossible = reservationImpossible;
    }
}
