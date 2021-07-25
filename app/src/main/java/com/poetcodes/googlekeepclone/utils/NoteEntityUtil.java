package com.poetcodes.googlekeepclone.utils;

import com.poetcodes.googlekeepclone.repository.models.Images;
import com.poetcodes.googlekeepclone.repository.models.NoteEssentials;
import com.poetcodes.googlekeepclone.repository.models.entities.Archive;
import com.poetcodes.googlekeepclone.repository.models.entities.Note;
import com.poetcodes.googlekeepclone.repository.models.entities.Trash;

import org.jetbrains.annotations.NotNull;

public class NoteEntityUtil {

    private final Note note;

    private NoteEntityUtil(@NotNull Builder builder) {
        NoteEssentials noteEssentials = builder.noteEssentials;
        note = new Note(
                null,
                noteEssentials.getTitle(),
                noteEssentials.getDescription(),
                noteEssentials.getCreatedAt(),
                noteEssentials.getUpdatedAt(),
                builder.image,
                builder.images,
                builder.deletedAt
        );
    }

    public NoteEntityUtil(@NotNull Note note) {
        this.note = new Note(
                null,
                note.getTitle(),
                note.getDescription(),
                note.getCreatedAt(),
                note.getUpdatedAt(),
                note.getImage(),
                note.getImages(),
                note.getDeletedAt()
        );
    }

    public NoteEntityUtil(@NotNull Trash trash) {
        Note mNote = trash.getNote();
        this.note = new Note(
                null,
                mNote.getTitle(),
                mNote.getDescription(),
                mNote.getCreatedAt(),
                mNote.getUpdatedAt(),
                mNote.getImage(),
                mNote.getImages(),
                null
        );
    }

    public NoteEntityUtil(@NotNull Archive archive) {
        Note mNote = archive.getNote();
        this.note = new Note(
                null,
                mNote.getTitle(),
                mNote.getDescription(),
                mNote.getCreatedAt(),
                mNote.getUpdatedAt(),
                mNote.getImage(),
                mNote.getImages(),
                mNote.getDeletedAt()
        );
    }

    public static class Builder {

        private NoteEssentials noteEssentials;
        private String image, deletedAt;
        private Images images;

        @NotNull
        public static Builder newInstance() {
            return new Builder();
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
