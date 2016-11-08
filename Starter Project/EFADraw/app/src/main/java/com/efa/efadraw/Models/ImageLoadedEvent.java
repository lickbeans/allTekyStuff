package com.efa.efadraw.Models;

/**
 * Created by aaroncampbell on 11/4/16.
 */

public class ImageLoadedEvent {
    public String selectedImage;

    public ImageLoadedEvent(String selectedImage) {
        this.selectedImage = selectedImage;
    }
}
