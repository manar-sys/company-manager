# Company Manager – Spring Boot + Security + Flyway + Testcontainers + Docker

Bu proje, gerçek işe daha yakın bir backend geliştirme akışını göstermek için hazırlanmıştır.

## Teknolojiler
- Spring Boot
- Spring Security (JWT + Role-based authorization)
- Spring Data JPA
- Flyway
- MySQL (dev ve docker)
- H2 (test profile)
- Testcontainers (integration test)
- Playwright (E2E)

## API ve Security
- Login endpoint: `POST /auth/login`
- Roller: `ADMIN`, `USER`
- Yetki kuralları:
- `GET /companies/**` erişilebilir
- `POST /companies`, `PUT /companies/{id}`, `DELETE /companies/{id}` sadece `ADMIN`
- Token yoksa `401`
- Rol yetersizse `403`

## Company Endpointleri
- Listeleme (pagination/sorting):  
`GET /companies?page=0&size=5&sortBy=id`
- Create: `POST /companies`
- Update: `PUT /companies/{id}`
- Delete: `DELETE /companies/{id}`

## Database Migration (Flyway)
- Şema migration dosyalarıyla otomatik oluşturulur.
- Migration dosyası: `src/main/resources/db/migration/V1__init_schema.sql`
- Uygulama açılırken Flyway migration çalışır.

## Integration Test (Testcontainers)
- Testlerde MySQL container otomatik ayağa kalkar.
- Test sınıfı: `src/test/java/com/example/demo/CompanySecurityIntegrationTest.java`
- Kapsanan senaryolar:
1. ADMIN token ile create/update/delete başarılı
2. USER delete denemesinde `403`
3. Token olmadan `401`

Çalıştırmak için:
```bash
./mvnw -Dtest=CompanySecurityIntegrationTest test
```

## Frontend ve E2E
Basit HTML + JavaScript arayüzü bulunur ve Playwright ile test edilir.

E2E testlerini çalıştırmak için:
```bash
cd e2e
npm install
npx playwright test
```

## Profiller
Dev (MySQL):
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Test (H2):
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

## Docker ile Çalıştırma
App + DB tek komutla:
```bash
docker compose up --build
```

Arka plan:
```bash
docker compose up -d --build
```

Durdurma:
```bash
docker compose down
```

Kontrol:
```bash
docker compose ps
docker compose logs -f app
```

## CI (GitHub Actions)
Push/PR sonrası backend build edilir, test profile ile app başlatılır ve Playwright testleri çalıştırılır.
