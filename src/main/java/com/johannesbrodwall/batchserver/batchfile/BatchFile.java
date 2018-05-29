package com.johannesbrodwall.batchserver.batchfile;

import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode(of="id")
public class BatchFile {
    
    @Getter @Setter
    private UUID id;
    
    @Getter @Setter
    private Path fileLocation;
    
    @Getter @Setter
    private Long fileLength;
    
    @Getter @Setter
    private Instant uploadTime;
    
    @Getter @Setter
    private String submittedFileName;

}
