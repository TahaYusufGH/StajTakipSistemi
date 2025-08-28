# Staj Takip Uygulaması

Bu uygulama, hukuk fakültesi stajyerleri için staj aşamalarını takip etmeye yönelik bir Java Swing uygulamasıdır.

## Özellikler

- ✅ Stajyer adı ve başlangıç tarihi girişi
- ✅ Otomatik staj aşaması hesaplama
- ✅ Hafta sonu ve resmi tatil günlerini dikkate alan tarih hesaplama
- ✅ Tablo formatında görüntüleme (her öğrenci tek satırda, tüm aşama başlangıç tarihleri yan yana)
- ✅ CSV dosyasına aktarma özelliği
- ✅ Seçili kişiyi silme özelliği
- ✅ Arama özelliği (kişi adına göre filtreleme)
- ✅ Otomatik veri saklama (uygulama kapanıp açıldığında veriler korunur)
- ✅ Modern ve kullanıcı dostu arayüz

## Staj Aşamaları

1. **Cumhuriyet Başsavcılığı** - 15 gün
2. **Bölge İdare** - 15 gün
3. **Ağır Ceza** - 1 ay (30 gün)
4. **Asliye Ceza** - 1 ay (30 gün)
5. **Sulh Hukuk** - 15 gün
6. **Asliye Hukuk/Asl. Tic/İş** - 1.5 ay (45 gün)
7. **İcra** - 1 ay (30 gün)
8. **Mazbata** - Tüm stajlar bittikten 2 gün sonra verilir

## Tarih Hesaplama Kuralları

- **İş günü hesaplaması**: Sadece iş günleri (Pazartesi-Cuma) sayılır
- **Hafta sonu atlanır**: Cumartesi ve Pazar günleri staj süresine dahil değil
- **Resmi tatiller atlanır**: Resmi tatil günleri staj süresine dahil değil
- **Doğru süre hesaplaması**: 15 iş günü = gerçekten 15 iş günü (tatiller hariç)
- Her aşama, bir önceki aşamanın bitiş tarihinden sonraki gün başlar

### 📅 Örnek Hesaplama
```
Başlangıç: 1 Ocak 2024 (Pazartesi)
Süre: 15 iş günü

Hesaplama:
- 1-5 Ocak: 5 iş günü (Pazartesi-Cuma)
- 6-7 Ocak: Hafta sonu - atlanır
- 8-12 Ocak: 5 iş günü (Pazartesi-Cuma) 
- 13-14 Ocak: Hafta sonu - atlanır
- 15-19 Ocak: 5 iş günü (Pazartesi-Cuma)

Toplam: 15 iş günü
Bitiş: 19 Ocak 2024 (Cuma)
```

## Kurulum ve Çalıştırma

### Gereksinimler
- Java 11 veya üzeri
- Maven 3.6 veya üzeri

### Kurulum
1. Projeyi klonlayın veya indirin
2. Terminal/komut isteminde proje klasörüne gidin
3. Bağımlılıkları indirin:
   ```bash
   mvn clean install
   ```

### Çalıştırma
```bash
mvn exec:java -Dexec.mainClass="Main"
```

veya JAR dosyası oluşturup çalıştırın:
```bash
mvn clean package
java -jar target/staj-takip-1.0-SNAPSHOT.jar
```

## Kullanım

1. **Stajyer Ekleme:**
   - "Ad Soyad" alanına stajyerin adını girin
   - "Başlangıç Tarihi" alanına stajın başlayacağı tarihi girin (yyyy-mm-dd formatında)
   - "Stajyer Ekle" butonuna tıklayın

2. **Arama:**
   - "Arama (Ad Soyad)" alanına kişi adının bir kısmını yazın
   - Tablo otomatik olarak filtrelenir
   - Büyük/küçük harf duyarlı değildir
   - Arama alanını boş bırakırsanız tüm kayıtlar görünür

3. **CSV'ye Aktarma:**
   - Tabloda veri olduktan sonra "CSV'ye Aktar" butonuna tıklayın
   - Kaydetmek istediğiniz konumu seçin
   - Dosya otomatik olarak .csv formatında kaydedilir (Excel'de açılabilir)

4. **Seçili Kişiyi Silme:**
   - Tabloda bir satıra tıklayarak kişiyi seçin
   - "Seçili Kişiyi Sil" butonuna tıklayın
   - Onay verdikten sonra kişi silinir

5. **Tabloyu Temizleme:**
   - "Tabloyu Temizle" butonu ile tüm verileri silebilirsiniz

## Veri Saklama

### 🗃️ Otomatik Kaydetme
- Veriler `staj_verileri.txt` dosyasında saklanır
- Her stajyer eklediğinizde otomatik kaydedilir
- Uygulama kapanıp açıldığında veriler geri yüklenir
- Tabloyu temizlediğinizde kayıt dosyası da temizlenir

### 📁 Veri Dosyası
- **Konum**: Uygulama klasöründe `staj_verileri.txt`
- **Format**: Pipe (|) ile ayrılmış değerler
- **Kodlama**: UTF-8 (Türkçe karakter desteği)
- **Yedekleme**: Dosyayı kopyalayarak yedekleyebilirsiniz

## Tablo Görüntüleme

Yeni tablo formatı şu sütunları içerir:
- **Ad Soyad**: Stajyerin adı
- **Başlangıç Tarihi**: Stajın genel başlangıç tarihi
- **Cumhuriyet Başsavcılığı**: Bu aşamanın başlangıç tarihi
- **Bölge İdare**: Bu aşamanın başlangıç tarihi
- **Ağır Ceza**: Bu aşamanın başlangıç tarihi
- **Asliye Ceza**: Bu aşamanın başlangıç tarihi
- **Sulh Hukuk**: Bu aşamanın başlangıç tarihi
- **Asliye Hukuk/Asl.Tic/İş**: Bu aşamanın başlangıç tarihi
- **İcra**: Bu aşamanın başlangıç tarihi
- **Mazbata**: Mazbata veriliş tarihi

**Örnek tablo görünümü:**
```
Ad Soyad    | Başlangıç | Başsavcı  | Bölge İd. | Ağır Ceza | Asliye C. | Sulh H.  | Asliye H. | İcra     | Mazbata
------------|-----------|-----------|-----------|-----------|-----------|----------|-----------|----------|----------
Ahmet Yılmaz| 01.01.2024| 01.01.2024| 16.01.2024| 31.01.2024| 02.03.2024| 01.04.2024| 16.04.2024| 31.05.2024| 02.07.2024
Ayşe Demir | 15.01.2024| 15.01.2024| 30.01.2024| 14.02.2024| 16.03.2024| 31.03.2024| 15.04.2024| 30.05.2024| 01.07.2024
```

## Resmi Tatiller

Uygulama aşağıdaki resmi tatilleri dikkate alır (2024 yılı için):
- Yılbaşı (1 Ocak)
- 23 Nisan Ulusal Egemenlik ve Çocuk Bayramı
- 1 Mayıs İşçi Bayramı
- 19 Mayıs Atatürk'ü Anma, Gençlik ve Spor Bayramı
- 15 Temmuz Demokrasi ve Milli Birlik Günü
- 30 Ağustos Zafer Bayramı
- 29 Ekim Cumhuriyet Bayramı
- Dini bayramlar (Ramazan ve Kurban Bayramı)

## Teknik Detaylar

- **UI Framework:** Java Swing
- **Export İşlemleri:** CSV format (Excel'de açılabilir)
- **Tarih İşlemleri:** Java 8+ LocalDate API
- **Build Tool:** Maven

## Lisans

Bu proje MIT lisansı altında lisanslanmıştır.
