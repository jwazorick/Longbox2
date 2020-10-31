package com.wazorick.longbox2.Objects;

public class WishlistItem extends Comic {
    private String wishlistPriority;
    private int wishlistID;

    public WishlistItem() {
        super();
        wishlistPriority = "";
    }

    public String getWishlistPriority() {
        return wishlistPriority;
    }

    public void setWishlistPriority(String wishlistPriority) {
        this.wishlistPriority = wishlistPriority;
    }

    public int getWishlistID() {
        return wishlistID;
    }

    public void setWishlistID(int wishlistID) {
        this.wishlistID = wishlistID;
    }
}
