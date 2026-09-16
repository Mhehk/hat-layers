# Hat Layers 1.2.0 — Minecraft 1.21.11

Sunucuda oynarken aktif chunk alanında yüklenen ve istemcide takip edilen oyuncuların
TAB kafa katmanını (hat layer) gösteren, kafa ikonuna küçük beyaz ve siyah konturlu
bir `*` ekleyen Fabric modu. Oyuncu alan veya takip dışına çıktığında katman ve
işaret kapanır; yüzün taban resmi görünür kalır. Yalnızca istemcide çalışır.

## Kurulum

1. Minecraft **Java Edition 1.21.11** için **Fabric Loader 0.19.3 veya üzerini** kur.
2. [Son sürümden](https://github.com/Mhehk/hat-layers/releases/latest) `hat-layers-1.2.0.jar` dosyasını indir ve kullandığın oyun profilinin `mods` klasörüne koy.
   Resmî başlatıcıda varsayılan klasör: `%APPDATA%\.minecraft\mods`.
3. Fabric profiliyle oyunu aç ve bir sunucuya gir. TAB'a bas.

Fabric API gerekmez. Sunucuya mod kurmak gerekmez. `-sources.jar` oyun için değildir.
Modrinth kullanıyorsan ilgili profilin klasörünü aç; varsayılan `.minecraft` klasörü
başka bir oyuna ait olabilir. Önceki Chunk Hat / Hat Layers JAR'ını kaldırıp oyunu yeniden başlat.

## Davranış

- Sabit 12 chunk sınırı yoktur. Oyun ayarındaki render mesafesi ve sunucunun
  bildirdiği görüş sınırından küçük olanı her TAB çiziminde kullanılır.
- Merkez, kendi oyuncunun bulunduğu chunk'tır. X ve Z yönlerinde bu mesafe içindeki,
  istemcide gerçekten yüklü chunk'lar değerlendirilir.
- Oyuncunun istemcinin mevcut dünyasında takip edilmesi gerekir. Bu oyuncuların
  TAB hat layer'ı açılır; diğer TAB oyuncularının katmanı kapatılır.
- Yükseklik, bakış yönü, duvarlar ve görüş hattı kontrol edilmez; madendeki oyuncular da dahildir.
- Kendi oyuncun da aynı kurala dahildir.
- Oyuncu alandan çıktığında veya sunucu onu takip listesinden kaldırdığında
  katman sonraki TAB çiziminde kapanır. Dünya değişimlerinde eski konumlar saklanmaz.
- Hat katmanı oyuncunun skin ayarında kapalı olsa bile TAB'da açılır.
  Skin'in ikinci kafa katmanı şeffaf olsa bile `*` işareti görünür.
- Çevrimdışı moddaki sunucularda da TAB kafa çizimi açılır; skin bulunamazsa
  Minecraft'ın varsayılan skini kullanılır.

Sunucu, entity takip mesafesi nedeniyle bir oyuncunun konumunu istemcine
göndermiyorsa mod onu tespit edemez. Render mesafesini artırmak, sunucunun oyuncu
takip mesafesini artırmaz. TAB'da adı bulunması, konumunun bilindiği anlamına gelmez.

## TAB yıldız işareti (1.2.0)

- Hat layer kararı açık olan oyuncuların TAB kafa ikonunun sağ üstünde küçük,
  beyaz ve siyah konturlu bir `*` gösterilir. İşaret, skin dokusundan bağımsızdır.
- İşaret yalnızca TAB listesinde çizilir; dünyadaki kafa veya isim etiketine eklenmez.
- Mevcut aktif chunk ve istemcide takip kuralları aynen kullanılır. Katman kapandığında
  işaret de aynı çizimde kaybolur; şeffaf ikinci katmanlı skinlerde de çalışır.
- Kontur dahil 3,5 × 3,5 GUI birimidir ve kafa ikonunun sağ üst köşesi içinde kalır.
  Oyuncu adına veya komşu satırlara taşmaz. Ters kafalarda da sağ üstte kalır.
- İki hesapla yakın/uzak geçişi, şeffaf hat katmanı, farklı GUI ölçekleri ve
  sunucu/dünya değişimi oyunda kontrol edilmelidir.

## Derleme ve kontroller

Java 21 JDK gereklidir. İlk derlemede bağımlılıkları indirmek için internet gerekir.

```powershell
.\gradlew.bat build
```

Çıktı: `build/libs/hat-layers-1.2.0.jar`.
`build`, değişken mesafe ve sınır durumları için 35 kontrolü de çalıştırır.

Gerçek Minecraft sınıflarında Mixin uygulamasını pencere açmadan doğrulamak için:

```powershell
.\gradlew.bat verifyMixins
```

## Uzak oyuncuda katman görünürse

1. TAB'ı aç, ardından `/hatlayers OyuncuAdı` yaz.
2. Komut, oyuncunun istemcide takip edilip edilmediğini, yatay chunk uzaklığını,
   etkin sınırı ve katman kararını gösterir. Son TAB çizimine gönderilen bayrak da
   zamanı ile birlikte gösterilir; bu geçmiş çizim, şu andaki konumdan farklı olabilir.
3. Bu komut yalnızca kendi sohbet ekranına yazı ekler; sunucuya veya diğer oyunculara gönderilmez.

`/hatlayers` isimsiz kullanılırsa en fazla 10 takip edilen oyuncuyu listeler.
Eski `/chunkhat` komutu da çalışmaya devam eder.
Yüzün taban resmi her zaman kalır; kontrol edilen yalnızca ikinci skin katmanıdır.

1.1.0 sürümünde katman kararı doğrudan kafa çizimine uygulanır. İlk sürümde
kontrol, çizimden önceki `shouldShowHat` çağrısındaydı. Başka modlarla daha iyi
birlikte çalışması için genel kafa görünürlüğü değişikliği de zincirlenebilir hale getirildi.

Görünen mod adı **Hat Layers**, geliştirici adı **mihek** ve sürüm **1.2.0**'dır.
Önceki sürümlerle aynı mod olarak tanınması için dahili Fabric kimliği `chunk_hat` korunur.

Oyunda iki hesapla kontrol:

1. İkinci hesabın görünür bir hat katmanı olan bir skin kullandığından emin ol.
2. Yakınında ve yüklü chunk'tayken TAB'da katmanının göründüğünü doğrula.
3. İkinci hesap madene girsin veya duvar arkasına geçsin; katman görünmeye devam etmeli.
4. İkinci hesap aktif alanın/takip mesafesinin dışına çıksın; TAB'da adı kalsa da katmanı kapanmalı.
5. Render mesafesini azaltıp artır; chunk yüklemeleri tamamlandığında sonucu tekrar kontrol et.
6. Sunucudan çıkıp başka dünyaya gir; önceki dünyanın oyuncularında katman kalmamalı.

Mod, vanilla TAB çizimine Mixin ile müdahale eder. TAB arayüzünü
tamamen değiştiren başka modlarla uyumluluk ayrıca oyunda kontrol edilmelidir.

## Teknik kaynaklar

- [Fabric — Minecraft 1.21.11](https://fabricmc.net/2025/12/05/12111.html)
- [Yarn — PlayerListHud API](https://maven.fabricmc.net/docs/yarn-1.21.11+build.6/net/minecraft/client/gui/hud/PlayerListHud.html)
- [MixinExtras — WrapOperation](https://github.com/LlamaLad7/MixinExtras/wiki/WrapOperation)
