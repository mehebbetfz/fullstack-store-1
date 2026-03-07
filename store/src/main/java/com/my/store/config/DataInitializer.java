package com.my.store.config;

import com.my.store.model.Product;
import com.my.store.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        // Создаём товары
        if (productRepository.count() == 0) {
            createProduct("MacBook Pro 14\" M3", "Мощный ноутбук Apple с чипом M3 Pro, 18 ГБ RAM, 512 ГБ SSD",
                    new BigDecimal("199999"), 10, Product.Category.LAPTOPS, "Apple", "MacBook Pro 14 M3",
                    "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=400");

            createProduct("iPhone 15 Pro 256GB", "Флагманский смартфон Apple с чипом A17 Pro, камерой 48 МП",
                    new BigDecimal("129999"), 25, Product.Category.SMARTPHONES, "Apple", "iPhone 15 Pro",
                    "https://images.unsplash.com/photo-1696446701796-da61225697cc?w=400");

            createProduct("Samsung Galaxy S24 Ultra", "Топовый Android-смартфон с S Pen, 200 МП камерой",
                    new BigDecimal("119999"), 30, Product.Category.SMARTPHONES, "Samsung", "Galaxy S24 Ultra",
                    "https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=400");

            createProduct("Dell XPS 15 9530", "Профессиональный ноутбук Intel Core i7, 32 ГБ, RTX 4060",
                    new BigDecimal("159999"), 8, Product.Category.LAPTOPS, "Dell", "XPS 15 9530",
                    "https://images.unsplash.com/photo-1588872657578-7efd1f1555ed?w=400");

            createProduct("iPad Pro 12.9\" M2", "Профессиональный планшет Apple с дисплеем Liquid Retina XDR",
                    new BigDecimal("109999"), 15, Product.Category.TABLETS, "Apple", "iPad Pro 12.9 M2",
                    "https://images.unsplash.com/photo-1544244015-0df4592987d0?w=400");

            createProduct("LG UltraWide 34\" QHD", "Изогнутый монитор 3440x1440, 144 Гц, HDR600",
                    new BigDecimal("59999"), 12, Product.Category.MONITORS, "LG", "34WP85C-B",
                    "https://images.unsplash.com/photo-1527443224154-c4a573d5f695?w=400");

            createProduct("Sony WH-1000XM5", "Беспроводные наушники с лучшим ANC, 30 часов работы",
                    new BigDecimal("34999"), 20, Product.Category.HEADPHONES, "Sony", "WH-1000XM5",
                    "https://images.unsplash.com/photo-1546435770-a3e426bf472b?w=400");

            createProduct("Logitech MX Keys S", "Беспроводная клавиатура с подсветкой, Bluetooth 5.1",
                    new BigDecimal("12999"), 40, Product.Category.KEYBOARDS, "Logitech", "MX Keys S",
                    "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=400");

            createProduct("Logitech MX Master 3S", "Профессиональная мышь 8000 dpi, бесшумные клики",
                    new BigDecimal("9999"), 50, Product.Category.MICE, "Logitech", "MX Master 3S",
                    "https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=400");

            createProduct("Canon EOS R6 Mark II", "Беззеркальная камера 24.2 МП, 4K 60fps, IBIS",
                    new BigDecimal("249999"), 5, Product.Category.CAMERAS, "Canon", "EOS R6 Mark II",
                    "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=400");

            createProduct("ASUS ROG Swift OLED 27\"", "Игровой OLED монитор 2560x1440, 240 Гц, 0.03 ms",
                    new BigDecimal("89999"), 7, Product.Category.MONITORS, "ASUS", "PG27AQDP",
                    "https://images.unsplash.com/photo-1593640408182-31c228f72eb3?w=400");

            createProduct("Microsoft Surface Pro 10", "Планшет-ноутбук Intel Core Ultra 7, 16 ГБ, 256 ГБ",
                    new BigDecimal("139999"), 10, Product.Category.TABLETS, "Microsoft", "Surface Pro 10",
                    "https://images.unsplash.com/photo-1593642632559-0c6d3fc62b89?w=400");
        }
    }

    private void createProduct(String name, String desc, BigDecimal price, int stock,
                               Product.Category category, String brand, String model, String imageUrl) {
        Product p = new Product();
        p.setName(name); p.setDescription(desc); p.setPrice(price);
        p.setStockQuantity(stock); p.setCategory(category);
        p.setBrand(brand); p.setModel(model); p.setImageUrl(imageUrl);
        productRepository.save(p);
    }
}

