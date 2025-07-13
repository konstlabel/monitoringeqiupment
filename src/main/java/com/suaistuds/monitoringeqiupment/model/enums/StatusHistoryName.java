package com.suaistuds.monitoringeqiupment.model.enums;


/**
 * Перечисление статусов записи в истории выдачи/возврата оборудования.
 * <ul>
 *   <li>{@link #cancelled} — выдача/возврат была отменена.</li>
 *   <li>{@link #rejected} — запрос на выдачу/возврат отклонён.</li>
 *   <li>{@link #returned} — оборудование возвращено.</li>
 *   <li>{@link #not_returned} — оборудование не возвращено.</li>
 * </ul>
 */
public enum StatusHistoryName {
    /** Операция отменена. */
    cancelled,

    /** Операция отклонена. */
    rejected,

    /** Оборудование возвращено. */
    returned,

    /** Оборудование ещё не возвращено. */
    not_returned
}