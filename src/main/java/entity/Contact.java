package entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Contact {

    VK("Вконтакте"),
    TELEGRAM("Телеграм"),
    INSTAGRAM("Инстаграм"),
    PHONE("Телефон");

    private final String description;
}
