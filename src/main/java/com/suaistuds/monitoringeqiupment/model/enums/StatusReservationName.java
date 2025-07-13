package com.suaistuds.monitoringeqiupment.model.enums;

/**
 * Перечисление статусов резервации оборудования.
 * <ul>
 *   <li>{@link #pending} — резервация ожидает подтверждения.</li>
 *   <li>{@link #confirmed} — резервация подтверждена.</li>
 *   <li>{@link #cancelled} — резервация отменена пользователем.</li>
 *   <li>{@link #rejected} — резервация отклонена администратором.</li>
 *   <li>{@link #returned} — оборудование по резервации возвращено.</li>
 *   <li>{@link #not_returned} — оборудование по резервации не возвращено.</li>
 * </ul>
 */
public enum StatusReservationName {
    /** Ожидает подтверждения. */
    pending,

    /** Подтверждена. */
    confirmed,

    /** Отменена. */
    cancelled,

    /** Отклонена. */
    rejected,

    /** Возвращено. */
    returned,

    /** Не возвращено. */
    not_returned
}