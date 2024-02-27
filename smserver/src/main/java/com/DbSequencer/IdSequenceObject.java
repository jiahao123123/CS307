package com.DbSequencer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "id_sequence")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdSequenceObject {

    @Id
    private String id;
    private int sequenceValue;
}
