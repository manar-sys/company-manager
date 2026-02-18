# Company Manager – Spring Boot + Playwright E2E

Bu proje mevcut Spring Boot backend’in üzerine basit bir frontend eklenerek uçtan uca (E2E) test edilmesi amacıyla hazırlanmıştır.

---

## Backend

### Kullanılan Teknolojiler

- Spring Boot
- Spring Data JPA
- MySQL (dev profili)
- H2 (test profili)
- Bean Validation (@Valid)
- Global Exception Handling (@ControllerAdvice)

### Özellikler

- Company CRUD endpointleri
- DTO + Validation
- Standart hata response yapısı
- Liste endpointinde pagination ve sorting desteği

Örnek:

```http
GET /api/companies?page=0&size=5&sort=name,asc
```

---

## Frontend

Basit bir HTML + JavaScript arayüzü:

- Company listesi
- Yeni company ekleme formu
- Güncelleme (form edit mode)
- Silme işlemi
- API validation hatalarının ekranda gösterilmesi

Not: `prompt()` yerine form-based edit kullanıldı. Bu tercih E2E testlerin daha stabil çalışması için yapıldı.

---

## Playwright E2E Testleri

Test senaryoları:

1. create → listede gör → update → delete
2. validation hatası (boş isim)

Testleri çalıştırmak için:

```bash
cd e2e
npm install
npx playwright test
```

---

## Profiller

### Dev Profili (MySQL)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Test Profili (H2 – CI için)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

---

## Docker ile Çalıştırma

Uygulamayı ve veritabanını tek komutla ayağa kaldırmak için:

```bash
docker compose up --build
```

Arka planda çalıştırmak için:

```bash
docker compose up -d --build
```

Durdurmak için:

```bash
docker compose down
```

---

## GitHub Actions (CI)

Push veya pull request sonrası:

- Backend build edilir
- Test profili ile Spring Boot başlatılır
- Playwright testleri otomatik çalıştırılır

Bu sayede uçtan uca akış sürekli doğrulanır.

---

## Neden Bu Yapı?

- Dev ve test ortamlarını ayırmak için Spring Profiles kullanıldı.
- CI ortamında dış bağımlılıkları azaltmak için H2 tercih edildi.
- E2E testlerin stabil olması için frontend edit akışı yeniden düzenlendi.
- Conventional Commits kullanılarak değişiklik geçmişi düzenli tutuldu.

Bu yapı gerçek projelerde kullanılan profesyonel geliştirme akışına uygun şekilde tasarlanmıştır.
