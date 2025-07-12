-- 1. Очистка старых таблиц
DROP TABLE IF EXISTS history;
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS equipment;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS status_history;
DROP TABLE IF EXISTS status_reservation;
DROP TABLE IF EXISTS status;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS type;

-- 2. Справочники

-- 2.1. Статусы оборудования
CREATE TABLE status (
  id   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(20)       NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY ux_status_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO status (name) VALUES
  ('available'),
  ('reserved'),
  ('issued');

-- 2.2. Статусы бронирования
CREATE TABLE status_reservation (
  id   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(20)       NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY ux_status_reservation_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO status_reservation (name) VALUES
  ('pending'),
  ('confirmed'),
  ('cancelled'),
  ('rejected'),
  ('returned'),
  ('not_returned');

-- 2.3. Статусы истории
CREATE TABLE status_history (
  id   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(20)       NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY ux_status_history_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO status_history (name) VALUES
  ('cancelled'),
  ('rejected'),
  ('returned'),
  ('not_returned');

-- 2.4. Роли пользователей
CREATE TABLE role (
  id   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(20)       NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY ux_role_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO role (name) VALUES
  ('admin'),
  ('studio'),
  ('user');

-- 2.5. Типы оборудования
CREATE TABLE type (
  id   BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  name VARCHAR(50)       NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY ux_type_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO type (name) VALUES
  ('camera'),
  ('lens'),
  ('microphone'),
  ('tripod'),
  ('light'),
  ('reflector'),
  ('audio_recorder'),
  ('headphones'),
  ('stabilizer'),
  ('backdrop');

-- 3. Основные сущности

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

-- Если у вас есть audit-поля у equipment (расширяет DateAudit),
-- добавьте сюда created_at, updated_at аналогично таблице user.

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

-- 4. Активные бронирования

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
  CONSTRAINT fk_res_user               FOREIGN KEY (user_id)                REFERENCES `user`               (id) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_res_status_reservation FOREIGN KEY (status_reservation_id) REFERENCES status_reservation (id) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_res_responsible        FOREIGN KEY (responsible_id)        REFERENCES `user`               (id) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. История финализированных бронирований

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
  CONSTRAINT fk_hist_user           FOREIGN KEY (user_id)           REFERENCES `user`           (id) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_hist_responsible    FOREIGN KEY (responsible_id)    REFERENCES `user`           (id) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT fk_hist_status_history FOREIGN KEY (status_history_id) REFERENCES status_history (id) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. Создать дефолтного админа (роль admin должна иметь id = 1)
-- 6.1. Замените здесь хэш пароля на свой (например, сгенерируйте BCrypt в вашем приложении)
INSERT INTO `user` (username, email, password, role_id)
VALUES (
  'superadmin',
  'admin@example.com',
  '$2a$10$8U0xmyvISPU9TaSqy5HES.jwn0EOzuCstrAEzKrqvm2KQneXxdCpq',
  1
);