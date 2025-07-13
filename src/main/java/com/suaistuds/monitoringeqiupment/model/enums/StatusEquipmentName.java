package com.suaistuds.monitoringeqiupment.model.enums;

/**
 * Перечисление статусов оборудования.
 * <ul>
 *   <li>{@link #available} — оборудование доступно для резервирования.</li>
 *   <li>{@link #reserved} — оборудование зарезервировано, но ещё не выдано.</li>
 *   <li>{@link #issued} — оборудование выдано пользователю.</li>
 * </ul>
 */
public enum StatusEquipmentName {
    /** Оборудование доступно. */
    available,

    /** Оборудование зарезервировано. */
    reserved,

    /** Оборудование выдано. */
    issued
}