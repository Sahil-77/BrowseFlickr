package com.admin_official.browseflickr;

// we have to implement serializable interface here because
// put extra method in main activity needs serializable objects
// put extra is used to pass information about photo object
// to search activity

import java.io.Serializable;

class Photo implements Serializable {
    // so that this code is compatible with every jvm
    private static final long serialVersionUID = 1L;

    private String title, author, authorId, link, tags, image;

    public Photo(String title, String author, String authorId, String tags, String image) {
        this.title = title;
        this.author = author.replace("nobody@flickr.com (\"", "");
        this.author = this.author.replace("\")", "");
        this.authorId = authorId;
        // to get the bigger image which will load on the different screen
        // for more information visit api garden of flickr api
        this.link = image.replaceFirst("_m.", "_b.");
        this.tags = tags;
        this.image = image;
    }

    String getTitle() {
        return title;
    }

    String getAuthor() {
        return author;
    }

    String getAuthorId() {
        return authorId;
    }

    String getLink() {
        return link;
    }

    String getTags() {
        return tags;
    }

    String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", authorId='" + authorId + '\'' +
                ", link='" + link + '\'' +
                ", tags='" + tags + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
