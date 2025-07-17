package com.suaistuds.monitoringequipment.model.enums;

/**
 * Перечисление статусов резервации оборудования.
 * <ul>
 *   <li>{@link #pending} — резервация ожидает подтверждения.</li>
 *   <li>{@link #confirmed} — резервация подтверждена.</li>
 *   <li>{@link #cancelled} — резервация отменена пользователем.</li>
 *   <li>{@link #issued} — оборудование выдано.</li>
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

    /** Выдано. */
    issued,

    /** Отклонена. */
    rejected,

    /** Возвращено. */
    returned,

    /** Не возвращено. */
    not_returned
}