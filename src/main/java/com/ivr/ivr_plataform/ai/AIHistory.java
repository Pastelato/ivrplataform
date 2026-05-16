package com.ivr.ivr_plataform.ai;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class AIHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @Column(length = 5000)
    private String inputText;

    @Column(length = 5000)
    private String result;

    private LocalDateTime createdAt;
}
