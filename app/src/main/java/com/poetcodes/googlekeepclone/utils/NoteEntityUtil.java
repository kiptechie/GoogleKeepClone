package com.poetcodes.googlekeepclone.utils;

import com.poetcodes.googlekeepclone.repository.models.Images;
import com.poetcodes.googlekeepclone.repository.models.NoteEssentials;
import com.poetcodes.googlekeepclone.repository.models.entities.Archive;
import com.poetcodes.googlekeepclone.repository.models.entities.Label;
import com.poetcodes.googlekeepclone.repository.models.entities.Note;
import com.poetcodes.googlekeepclone.repository.models.entities.Trash;

import org.jetbrains.annotations.NotNull;

public class NoteEntityUtil {

    private final Note note;

    private NoteEntityUtil(@NotNull Builder builder) {
        NoteEssentials noteEssentials = builder.noteEssentials;
        note = new Note(
                noteEssentials.getId(),
                noteEssentials.getTitle(),
                noteEssentials.getDescription(),
                noteEssentials.getCreatedAt(),
                noteEssentials.getUpdatedAt(),
                builder.image,
                builder.images,
                builder.deletedAt,
                builder.label,
                builder.backgroundColor
        );
    }

    public NoteEntityUtil(@NotNull Note note) {
        this.note = new Note(
                note.getId(),
                note.getTitle(),
                note.getDescription(),
                note.getCreatedAt(),
                note.getUpdatedAt(),
                note.getImage(),
                note.getImages(),
                note.getDeletedAt(),
                note.getLabel(),
                note.getBackgroundColor()
        );
    }

    public NoteEntityUtil(@NotNull Trash trash) {
        Note mNote = trash.getNote();
        note = new Note(
                mNote.getId(),
                mNote.getTitle(),
                mNote.getDescription(),
                mNote.getCreatedAt(),
                mNote.getUpdatedAt(),
                mNote.getImage(),
                mNote.getImages(),
                null,
                mNote.getLabel(),
                mNote.getBackgroundColor()
        );
    }

    public NoteEntityUtil(@NotNull Archive archive) {
        Note mNote = archive.getNote();
        note = new Note(
                mNote.getId(),
                mNote.getTitle(),
                mNote.getDescription(),
                mNote.getCreatedAt(),
                mNote.getUpdatedAt(),
                mNote.getImage(),
                mNote.getImages(),
                mNote.getDeletedAt(),
                mNote.getLabel(),
                mNote.getBackgroundColor()
        );
    }

    public static class Builder {

        private NoteEssentials noteEssentials;
        private String image, deletedAt, backgroundColor;
        private Images images;
        private Label label;

        @NotNull
        public static Builder newInstance() {
            return new Builder();
        }

        public Builder withLabel(Label label) {
            this.label = label;
            return this;
        }

        public Builder withBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        @NotNull
        public Builder withNoteEssentials(@NotNull NoteEssentials noteEssentials) {
            this.noteEssentials = noteEssentials;
            return this;
        }

        public Builder withImage(String image) {
            this.image = image;
            return this;
        }

        public Builder withImages(Images images) {
            this.images = images;
            return this;
        }

        public Builder setDeletedAt(String deletedAt) {
            this.deletedAt = deletedAt;
            return this;
        }

        @NotNull
        public NoteEntityUtil build() {
            return new NoteEntityUtil(this);
        }
    }

    public Note getNote() {
        return note;
    }
}
