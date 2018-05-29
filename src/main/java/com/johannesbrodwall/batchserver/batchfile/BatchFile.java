package com.johannesbrodwall.batchserver.batchfile;

import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@EqualsAndHashCode(of="id")
public class BatchFile {
    
    @Getter
    private UUID id;

}
