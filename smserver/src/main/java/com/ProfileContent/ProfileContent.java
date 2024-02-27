package com.ProfileContent;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileContent<T> {
    private T content;
    private boolean publicInfo;

    public boolean isPublic() { return this.publicInfo; }

    public String toString() { return this.content.toString(); }

    public int toInt() { return (int) this.content; }
}


