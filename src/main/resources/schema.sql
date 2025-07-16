-- ============================================================================
-- SCHEMA.SQL: Инициализация базы данных для системы бронирования оборудования
-- ============================================================================

-- ----------------------------------------------------------------------------
-- 1. Очистка старых таблиц (если они существуют, для повторной инициализации)
-- ----------------------------------------------------------------------------
DROP TABLE IF EXISTS history;
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS equipment;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS status_history;
DROP TABLE IF EXISTS status_reservation;
DROP TABLE IF EXISTS status;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS type;

-- ----------------------------------------------------------------------------
-- 2. Справочные таблицы (категории, статусы, роли)
-- ----------------------------------------------------------------------------

-- 2.1. Таблица статусов оборудования
-- Возможные значения: available, reserved, issued
CREATE TABLE status (
  id   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(20)       NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY ux_status_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Предустановленные статусы оборудования
INSERT INTO status (name) VALUES
  ('available'),        -- доступно
  ('reserved'),         -- зарезервировано
  ('issued');           -- выдано

-- 2.2. Таблица статусов бронирования
-- Описывает состояние активных бронирований
CREATE TABLE status_reservation (
  id   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(20)       NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY ux_status_reservation_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Предустановленные статусы бронирования
INSERT INTO status_reservation (name) VALUES
  ('pending'),          -- ожидает подтверждения
  ('confirmed'),        -- подтверждено
  ('cancelled'),        -- отменено
  ('issued'),           -- выдано
  ('rejected'),         -- отклонено
  ('returned'),         -- возвращено
  ('not_returned');     -- не возвращено

-- 2.3. Таблица статусов истории
-- Используется только для финализированных записей истории
CREATE TABLE status_history (
  id   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(20)       NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY ux_status_history_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Предустановленные статусы истории
INSERT INTO status_history (name) VALUES
  ('cancelled'),        -- отменено
  ('rejected'),         -- отклонено
  ('returned'),         -- возвращено
  ('not_returned');     -- не возвращено

-- 2.4. Таблица ролей пользователей
-- Используется для авторизации и разграничения доступа
CREATE TABLE role (
  id   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(20)       NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY ux_role_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Предустановленные роли
INSERT INTO role (name) VALUES
  ('admin'),            -- админ
  ('studio'),           -- студия
  ('user');             -- пользователь

-- 2.5. Таблица типов оборудования
-- Категории техники, доступной для бронирования
CREATE TABLE type (
  id   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(50)       NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY ux_type_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Предустановленные типы оборудования
INSERT INTO type (name) VALUES
  ('camera'),           -- камера
  ('lens'),             -- объектив
  ('microphone'),       -- микрофон
  ('tripod'),           -- штатив
  ('light'),            -- свет
  ('reflector'),        -- отражатель света
  ('audio_recorder'),   -- диктофон
  ('headphones'),       -- наушники
  ('stabilizer'),       -- стабилизатор
  ('backdrop');         -- фон


-- ----------------------------------------------------------------------------
-- 3. Основные сущности (оборудование и пользователи)
-- ----------------------------------------------------------------------------

-- Таблица оборудования
-- Хранит технические единицы: наименование, серийный номер, статус и тип
CREATE TABLE equipment (
  id            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name          VARCHAR(100)       NOT NULL,
  serial_number VARCHAR(100)       NOT NULL,
  status_id     BIGINT UNSIGNED    NOT NULL,
  type_id       BIGINT UNSIGNED    NOT NULL,
  created_at DATETIME NULL,
  created_by BIGINT UNSIGNED NULL,
  updated_at DATETIME NULL,
  updated_by BIGINT UNSIGNED NULL,
  PRIMARY KEY (id),
  UNIQUE KEY ux_equipment_serial (serial_number),
  KEY fk_equipment_status (status_id),
  KEY fk_equipment_type   (type_id),
  CONSTRAINT fk_equipment_status FOREIGN KEY (status_id) REFERENCES status (id) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_equipment_type   FOREIGN KEY (type_id)   REFERENCES type   (id) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Таблица пользователей
-- Хранит информацию об учетных записях и их ролях
CREATE TABLE `user` (
  id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  username   VARCHAR(50)       NOT NULL,
  email      VARCHAR(100)      NOT NULL,
  password   VARCHAR(100)      NOT NULL,
  role_id    BIGINT UNSIGNED   NOT NULL,
  created_at DATETIME          NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME          NOT NULL DEFAULT CURRENT_TIMESTAMP
                               ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY ux_user_username (username),
  UNIQUE KEY ux_user_email    (email),
  KEY fk_user_role (role_id),
  CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES role (id) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ----------------------------------------------------------------------------
-- 4. Таблица активных бронирований
-- ----------------------------------------------------------------------------

CREATE TABLE reservation (
  id                    BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  equipment_id          BIGINT UNSIGNED    NOT NULL,
  user_id               BIGINT UNSIGNED    NOT NULL,
  start_date            DATE               NOT NULL,
  end_date              DATE               NOT NULL,
  status_reservation_id BIGINT UNSIGNED    NOT NULL,
  responsible_id        BIGINT UNSIGNED    NOT NULL,
  created_at DATETIME NULL,
  created_by BIGINT UNSIGNED NULL,
  updated_at DATETIME NULL,
  updated_by BIGINT UNSIGNED NULL,
  PRIMARY KEY (id),
  KEY fk_res_equipment          (equipment_id),
  KEY fk_res_user               (user_id),
  KEY fk_res_status_reservation (status_reservation_id),
  KEY fk_res_responsible        (responsible_id),
  CONSTRAINT fk_res_equipment          FOREIGN KEY (equipment_id)           REFERENCES equipment          (id) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_res_user               FOREIGN KEY (user_id)                REFERENCES `user`             (id) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_res_status_reservation FOREIGN KEY (status_reservation_id)  REFERENCES status_reservation (id) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_res_responsible        FOREIGN KEY (responsible_id)         REFERENCES `user`             (id) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ----------------------------------------------------------------------------
-- 5. Таблица истории завершенных бронирований
-- ----------------------------------------------------------------------------

CREATE TABLE history (
  id                BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  equipment_id      BIGINT UNSIGNED    NOT NULL,
  user_id           BIGINT UNSIGNED    NOT NULL,
  responsible_id    BIGINT UNSIGNED    NOT NULL,
  date              DATETIME           NOT NULL,
  status_history_id BIGINT UNSIGNED    NOT NULL,
  created_at DATETIME NULL,
  created_by BIGINT UNSIGNED NULL,
  updated_at DATETIME NULL,
  updated_by BIGINT UNSIGNED NULL,
  PRIMARY KEY (id),
  KEY fk_hist_equipment      (equipment_id),
  KEY fk_hist_user           (user_id),
  KEY fk_hist_responsible    (responsible_id),
  KEY fk_hist_status_history (status_history_id),
  CONSTRAINT fk_hist_equipment      FOREIGN KEY (equipment_id)      REFERENCES equipment      (id) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_hist_user           FOREIGN KEY (user_id)           REFERENCES `user`         (id) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_hist_responsible    FOREIGN KEY (responsible_id)    REFERENCES `user`         (id) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_hist_status_history FOREIGN KEY (status_history_id) REFERENCES status_history (id) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ----------------------------------------------------------------------------
-- 6. Создание дефолтного администратора (роль admin должна быть с id = 1)
-- ----------------------------------------------------------------------------

INSERT INTO `user` (username, email, password, role_id)
VALUES (
  'superadmin',
  'admin@example.com',
  '$2a$10$8U0xmyvISPU9TaSqy5HES.jwn0EOzuCstrAEzKrqvm2KQneXxdCpq',
  1
);