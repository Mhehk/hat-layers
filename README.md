# Hat Layers

Minecraft **1.21.11** için istemci taraflı Fabric modu.

Yakındaki, yüklü chunk alanında bulunan ve istemcide takip edilen oyuncuların TAB kafa katmanını gösterir. Kafa ikonunun sağ üstüne küçük, beyaz ve siyah konturlu bir `*` ekler. Oyuncu bu alanın veya takip mesafesinin dışına çıktığında katman ve işaret kaybolur; yüz görünür kalır.

## Özellikler

- Gösterim mesafesi, oyun ve sunucunun görüş sınırına göre belirlenir.
- Duvar arkasındaki ve yer altındaki oyuncular da aynı koşullarla gösterilir.
- Skin'in ikinci kafa katmanı şeffaf olsa bile `*` işareti görünür.
- Yalnızca TAB listesini etkiler.

## Kurulum

1. Minecraft **1.21.11** için **Fabric Loader 0.19.3 veya üzerini** kur.
2. [Son sürümden](https://github.com/Mhehk/hat-layers/releases/latest) `hat-layers-1.2.0.jar` dosyasını indir.
3. Dosyayı oyun profilinin `mods` klasörüne koy ve oyunu yeniden başlat. Eski Hat Layers sürümü varsa kaldır.

**Fabric API ve sunucuya kurulum gerekmez.** Java 21 veya üzeri gereklidir.

## Komutlar

- `/hatlayers` — Takip edilen oyuncuların durumunu listeler.
- `/hatlayers OyuncuAdı` — Oyuncunun mesafesini ve katman durumunu gösterir.

Komut çıktıları yalnızca senin ekranında görünür. Sunucunun konumunu göndermediği oyuncular tespit edilemez.

## Kaynaktan derleme

Java 21 JDK ile Windows'ta `gradlew.bat build`, Linux/macOS'ta `./gradlew build` çalıştır.
Derlenen JAR dosyası `build/libs` klasörüne kaydedilir.

## Lisans

[MIT](LICENSE) · mihek
