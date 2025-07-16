package com.suaistuds.monitoringeqiupment.payload;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DTO (Data Transfer Object) для постраничного (пагинированного) ответа API.
 * Обеспечивает стандартизированную структуру для возврата данных с разбивкой по страницам.
 *
 * <p>Особенности:
 * <ul>
 *   <li>Поддерживает любые типы данных (generic type T)</li>
 *   <li>Обеспечивает неизменяемость содержимого (immutable content)</li>
 *   <li>Содержит полную метаинформацию о пагинации</li>
 * </ul>
 *
 * @param <T> тип элементов в постраничном ответе
 * @since 2025-07-13
 */
@Data
public class PagedResponse<T> {

    /**
     * Содержимое текущей страницы
     * <p><b>Характеристики:</b>
     * <ul>
     *   <li>Неизменяемый список (immutable)</li>
     *   <li>Может содержать null</li>
     *   <li>Возвращает защищенную копию при получении</li>
     * </ul>
     */
    private List<T> content;

    /**
     * Номер текущей страницы (начинается с 0)
     */
    private int page;

    /**
     * Количество элементов на странице
     */
    private int size;

    /**
     * Общее количество элементов во всех страницах
     */
    private long totalElements;

    /**
     * Общее количество страниц
     */
    private int totalPages;

    /**
     * Флаг, указывающий является ли текущая страница последней
     */
    private boolean last;

    /**
     * Конструктор по умолчанию
     */
    public PagedResponse() {}

    /**
     * Основной конструктор для создания постраничного ответа
     * @param content содержимое страницы
     * @param page номер текущей страницы
     * @param size количество элементов на странице
     * @param totalElements общее количество элементов
     * @param totalPages общее количество страниц
     * @param last флаг последней страницы
     */
    public PagedResponse(List<T> content, int page, int size, long totalElements, int totalPages, boolean last) {
        setContent(content);
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.last = last;
    }

    /**
     * Возвращает защищенную копию содержимого страницы
     * @return новый ArrayList с содержимым или null
     */
    public List<T> getContent() {
        return content == null ? null : new ArrayList<>(content);
    }

    /**
     * Устанавливает неизменяемое содержимое страницы
     * @param content список элементов для страницы
     */
    public final void setContent(List<T> content) {
        if (content == null) {
            this.content = null;
        } else {
            this.content = Collections.unmodifiableList(content);
        }
    }

    /**
     * Проверяет является ли текущая страница последней
     * @return true если это последняя страница, false в противном случае
     */
    public boolean isLast() {
        return last;
    }
}