

-- Đảm bảo dùng bộ ký tự UTF-8
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- TABLE: role
CREATE TABLE `role` (
                        `role_id` INT PRIMARY KEY AUTO_INCREMENT,
                        `role_name` VARCHAR(10)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- TABLE: user
CREATE TABLE `user` (
                        `user_id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                        `username` VARCHAR(100) UNIQUE,
                        `full_name` VARCHAR(50),
                        `date_of_birth` DATE,
                        `gender` VARCHAR(6),
                        `email` VARCHAR(255) UNIQUE ,
                        `password` VARCHAR(255),
                        `phone` VARCHAR(20),
                        `created_at` DATETIME,
                        `updated_at` DATETIME,
                        `status` VARCHAR(255),
                        `role_id` INT DEFAULT 3,
                        FOREIGN KEY (`role_id`) REFERENCES `role`(`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- TABLE: user_address
CREATE TABLE `user_address` (
                                `address_id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                `user_id` BIGINT,
                                `address_line` TEXT,
                                `city` VARCHAR(50),
                                `postcode` VARCHAR(100),
                                `country` VARCHAR(50),
                                `is_default` BOOLEAN,
                                `created_at` DATETIME,
                                `updated_at` DATETIME,
                                FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- TABLE: recipient
CREATE TABLE `recipient` (
                             `recipient_id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                             `user_id` BIGINT,
                             `name` VARCHAR(100),
                             `email` VARCHAR(255),
                             `phone` VARCHAR(20),
                             `address_line` TEXT,
                             `city` VARCHAR(50),
                             `postcode` VARCHAR(100),
                             `country` VARCHAR(50),
                             `is_default` BOOLEAN,
                             `created_at` DATETIME,
                             `updated_at` DATETIME,
                             FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- TABLE: credit_card
CREATE TABLE `credit_card` (
                               `card_id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                               `user_id` BIGINT,
                               `card_holder_name` VARCHAR(100),
                               `card_number` VARCHAR(16),
                               `expiry_month` INT,
                               `expiry_year` INT,
                               `is_default` BOOLEAN,
                               `created_at` DATETIME,
                               `updated_at` DATETIME,
                               FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- TABLE: category
CREATE TABLE `category` (
                            `category_id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                            `name` VARCHAR(50) UNIQUE,
                            `description` TEXT,
                            `created_at` DATETIME,
                            `updated_at` DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- TABLE: product
CREATE TABLE `product` (
                           `product_id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                           `category_id` BIGINT,
                           `name` VARCHAR(100),
                           `description` VARCHAR(255),
                           `unit` VARCHAR(10),
                           `price` DECIMAL(10,2),
                           `stock_quantity` INT,
                           `stock_status` VARCHAR(15),
                           `created_at` DATETIME,
                           `updated_at` DATETIME,
                           FOREIGN KEY (`category_id`) REFERENCES `category`(`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- TABLE: package
CREATE TABLE `package` (
                           `package_id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                           `name` VARCHAR(100),
                           `description` TEXT,
                           `base_price` DECIMAL(10,2),
                           `created_at` DATETIME,
                           `updated_at` DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- TABLE: package_product
CREATE TABLE `package_product` (
                                   `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                   `package_id` BIGINT,
                                   `product_id` BIGINT,
                                   `quantity` INT,
                                   `created_at` DATETIME,
                                   `updated_at` DATETIME,
                                   FOREIGN KEY (`package_id`) REFERENCES `package`(`package_id`),
                                   FOREIGN KEY (`product_id`) REFERENCES `product`(`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- TABLE: custom_package
CREATE TABLE `custom_package` (
                                  `custom_package_id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  `user_id` BIGINT,
                                  `name` VARCHAR(100),
                                  `created_at` DATETIME,
                                  `updated_at` DATETIME,
                                  FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- TABLE: custom_package_product
CREATE TABLE `custom_package_product` (
                                          `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                          `custom_package_id` BIGINT,
                                          `product_id` BIGINT,
                                          `quantity` INT,
                                          `created_at` DATETIME,
                                          `updated_at` DATETIME,
                                          FOREIGN KEY (`custom_package_id`) REFERENCES `custom_package`(`custom_package_id`),
                                          FOREIGN KEY (`product_id`) REFERENCES `product`(`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- TABLE: cart
CREATE TABLE `cart` (
                        `cart_id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                        `user_id` BIGINT UNIQUE,
                        `status` VARCHAR(15),
                        `total_price` DECIMAL(10,2),
                        `created_at` DATETIME,
                        `updated_at` DATETIME,
                        FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- TABLE: cart_item
CREATE TABLE `cart_item` (
                             `cart_item_id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                             `cart_id` BIGINT,
                             `item_type` VARCHAR(50),
                             `item_id` BIGINT,
                             `quantity` INT,
                             `created_at` DATETIME,
                             `updated_at` DATETIME,
                             FOREIGN KEY (`cart_id`) REFERENCES `cart`(`cart_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- TABLE: order_status
CREATE TABLE `order_status` (
                                `status_id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                `name` VARCHAR(15),
                                `created_at` DATETIME,
                                `updated_at` DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- TABLE: order_channel
CREATE TABLE `order_channel` (
                                 `channel_id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 `name` VARCHAR(15),
                                 `created_at` DATETIME,
                                 `updated_at` DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- TABLE: order
CREATE TABLE `order` (
                         `order_id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                         `user_id` BIGINT,
                         `address_id` BIGINT,
                         `csr_id` BIGINT,
                         `recipient_id` BIGINT,
                         `order_date` DATETIME,
                         `total_amount` DECIMAL(10,2),
                         `discount_applied` DECIMAL(10,2),
                         `order_status_id` BIGINT,
                         `order_channel_id` BIGINT,
                         `created_at` DATETIME,
                         `updated_at` DATETIME,
                         FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`),
                         FOREIGN KEY (`address_id`) REFERENCES `user_address`(`address_id`),
                         FOREIGN KEY (`csr_id`) REFERENCES `user`(`user_id`),
                         FOREIGN KEY (`recipient_id`) REFERENCES `recipient`(`recipient_id`),
                         FOREIGN KEY (`order_status_id`) REFERENCES `order_status`(`status_id`),
                         FOREIGN KEY (`order_channel_id`) REFERENCES `order_channel`(`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- TABLE: order_item
CREATE TABLE `order_item` (
                              `order_item_id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                              `order_id` BIGINT,
                              `item_type` VARCHAR(50),
                              `item_id` BIGINT,
                              `quantity` INT,
                              `unit_price` DECIMAL(10,2),
                              `subtotal` DECIMAL(10,2),
                              `created_at` DATETIME,
                              `updated_at` DATETIME,
                              FOREIGN KEY (`order_id`) REFERENCES `order`(`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- TABLE: payment_status
CREATE TABLE `payment_status` (
                                  `status_id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  `name` VARCHAR(15),
                                  `created_at` DATETIME,
                                  `updated_at` DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- TABLE: payment
CREATE TABLE `payment` (
                           `payment_id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                           `order_id` BIGINT,
                           `user_id` BIGINT,
                           `amount` DECIMAL(10,2),
                           `payment_date` DATETIME,
                           `payment_status_id` BIGINT,
                           `visa_check_reference` VARCHAR(255),
                           `created_at` DATETIME,
                           `updated_at` DATETIME,
                           FOREIGN KEY (`order_id`) REFERENCES `order`(`order_id`),
                           FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`),
                           FOREIGN KEY (`payment_status_id`) REFERENCES `payment_status`(`status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- TABLE: enquiry
CREATE TABLE `enquiry` (
                           `enquiry_id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                           `user_id` BIGINT,
                           `package_id` BIGINT,
                           `csr_id` BIGINT,
                           `enquiry_details` TEXT,
                           `enquiry_date` DATETIME,
                           `status` VARCHAR(15),
                           `response_text` TEXT,
                           `response_date` DATETIME,
                           `created_at` DATETIME,
                           `updated_at` DATETIME,
                           FOREIGN KEY (`user_id`) REFERENCES `user`(`user_id`),
                           FOREIGN KEY (`package_id`) REFERENCES `package`(`package_id`),
                           FOREIGN KEY (`csr_id`) REFERENCES `user`(`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- TABLE: asset
CREATE TABLE `asset` (
                         `asset_id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                         `file_url` VARCHAR(200),
                         `file_type` VARCHAR(20),
                         `usage` VARCHAR(20),
                         `usage_id` BIGINT,
                         `is_primary` BOOLEAN,
                         `alt_text` VARCHAR(100),
                         `uploaded_at` DATETIME,
                         `created_at` DATETIME,
                         `updated_at` DATETIME,
                         FOREIGN KEY (`usage_id`) REFERENCES `product`(`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE feedback (
                          feedback_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          user_id BIGINT,
                          target_type VARCHAR(50),  -- 'product', 'package', 'order'
                          target_id BIGINT,
                          rating INT,
                          comment TEXT,
                          created_at DATETIME,
                          updated_at DATETIME,
                          FOREIGN KEY (user_id) REFERENCES user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO role (role_name) VALUES
                            ('STAFF'),
                            ('ADMIN'),
                            ('CUSTOMER'),
                            ('GUEST');

SELECT * FROM role;
