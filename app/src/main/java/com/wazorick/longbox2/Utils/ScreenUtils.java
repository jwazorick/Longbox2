package com.wazorick.longbox2.Utils;

import android.content.Context;
import android.widget.Toast;

public class ScreenUtils {
    public static void showAddSuccessToast(Context context) {
        Toast.makeText(context, "Successfully added item", Toast.LENGTH_SHORT).show();
    }

    public static void showAddFailureToast(Context context) {
        Toast.makeText(context, "Could not add item. Please try again", Toast.LENGTH_SHORT).show();
    }

    public static void showNotEnoughInfoToast(Context context) {
        Toast.makeText(context, "Please enter more information", Toast.LENGTH_SHORT).show();
    }

    public static void showNoItemSelectedToast(Context context) {
        Toast.makeText(context, "Please select at least one item", Toast.LENGTH_SHORT).show();
    }

    public static void showEditSuccessToast(Context context) {
        Toast.makeText(context, "Item updated successfully", Toast.LENGTH_SHORT).show();
    }

    public static void showEditFailureToast(Context context) {
        Toast.makeText(context, "Item not updated. Please try again", Toast.LENGTH_SHORT).show();
    }

    public static void showTransferSuccessToast(Context context) {
        Toast.makeText(context, "Transfer successful", Toast.LENGTH_SHORT).show();
    }

    public static void showTransferFailureToast(Context context) {
        Toast.makeText(context, "Not all items transferred. Please try again", Toast.LENGTH_SHORT).show();
    }

    public static void showDeletedSuccessToast(Context context) {
        Toast.makeText(context, "Items deleted successfully", Toast.LENGTH_SHORT).show();
    }

    public static void showDeleteFailureToast(Context context) {
        Toast.makeText(context, "Not all items deleted. Please try again", Toast.LENGTH_SHORT).show();
    }
}
