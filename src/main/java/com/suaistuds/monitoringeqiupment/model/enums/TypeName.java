package com.suaistuds.monitoringeqiupment.model.enums;

/**
 * Перечисление типов оборудования.
 * <ul>
 *   <li>{@link #camera} — фото- или видеокамера.</li>
 *   <li>{@link #lens} — объектив.</li>
 *   <li>{@link #microphone} — микрофон.</li>
 *   <li>{@link #tripod} — штатив.</li>
 *   <li>{@link #light} — осветительный прибор.</li>
 *   <li>{@link #reflector} — отражатель света.</li>
 *   <li>{@link #audio_recorder} — звуковой рекордер.</li>
 *   <li>{@link #headphones} — наушники.</li>
 *   <li>{@link #stabilizer} — стабилизатор (стедикам и пр.).</li>
 *   <li>{@link #backdrop} — фон (баннер, ткань и пр.).</li>
 * </ul>
 */
public enum TypeName {
    /** Фото- или видеокамера. */
    camera,

    /** Объектив. */
    lens,

    /** Микрофон. */
    microphone,

    /** Штатив. */
    tripod,

    /** Осветительный прибор. */
    light,

    /** Отражатель света. */
    reflector,

    /** Звуковой рекордер. */
    audio_recorder,

    /** Наушники. */
    headphones,

    /** Стабилизатор (стедикам и пр.). */
    stabilizer,

    /** Фон (баннер, ткань и пр.). */
    backdrop
}