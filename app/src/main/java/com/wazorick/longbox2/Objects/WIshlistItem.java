package com.example.longbox2.Objects;

public class WIshlistItem extends Comic {
    private String wishlistPriority;

    public WIshlistItem() {
        super();
        wishlistPriority = "";
    }

    public String getWishlistPriority() {
        return wishlistPriority;
    }

    public void setWishlistPriority(String wishlistPriority) {
        this.wishlistPriority = wishlistPriority;
    }
}
