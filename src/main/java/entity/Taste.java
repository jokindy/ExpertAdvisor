package entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Taste {

    SWEET("Сладенькое"),
    SOUR("Кисленькое"),
    STRANGE("Странное"),
    APPLE("Двойное яблоко");

    private final String description;
}
