package com.aaroncampbell.peoplemon.Models;

/**
 * Created by aaroncampbell on 11/9/16.
 */

public class ImageLoadedEvent {
    public String selectedImage;

    public ImageLoadedEvent(String selectedImage) {
        this.selectedImage = selectedImage;
    }
}