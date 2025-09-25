-- Pastikan session pakai utf8mb4
/*!40101 SET NAMES utf8mb4 */;

-- MySQL dump 10.13  Distrib 8.0.43, for Linux (x86_64)
--
-- Host: localhost    Database: notesdb
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `conjugation_override`
--

LOCK TABLES `conjugation_override` WRITE;
/*!40000 ALTER TABLE `conjugation_override` DISABLE KEYS */;
INSERT INTO `conjugation_override` VALUES (1,3,'DICTIONARY','する','irregular'),(2,3,'MASU','します','irregular'),(3,3,'TE','して','irregular'),(4,3,'TA','した','irregular'),(5,3,'NAI','しない','irregular'),(6,3,'POTENTIAL','できる','する→できる'),(7,3,'VOLITIONAL','しよう','irregular'),(8,3,'CONDITIONAL','すれば','irregular'),(9,3,'CONDITIONAL_NEG','しなければ','irregular'),(10,3,'IMPERATIVE','しろ','irregular'),(11,3,'PASSIVE','される','irregular'),(12,3,'CAUSATIVE','させる','irregular'),(13,3,'CAUSATIVE_PASSIVE','させられる','irregular'),(14,4,'DICTIONARY','くる','irregular'),(15,4,'MASU','きます','irregular'),(16,4,'TE','きて','irregular'),(17,4,'TA','きた','irregular'),(18,4,'NAI','こない','irregular'),(19,4,'POTENTIAL','こられる','irregular'),(20,4,'VOLITIONAL','こよう','irregular'),(21,4,'CONDITIONAL','くれば','irregular'),(22,4,'CONDITIONAL_NEG','こなければ','irregular'),(23,4,'IMPERATIVE','こい','irregular'),(24,4,'PASSIVE','こられる','irregular'),(25,4,'CAUSATIVE','こさせる','irregular'),(26,4,'CAUSATIVE_PASSIVE','こさせられる','irregular'),(27,6,'CONDITIONAL','よければ','i-adj irregular'),(28,6,'CONDITIONAL_NEG','よくなければ','i-adj irregular');
/*!40000 ALTER TABLE `conjugation_override` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Dumping data for table `jlpt_examples`
--

LOCK TABLES `jlpt_examples` WRITE;
/*!40000 ALTER TABLE `jlpt_examples` DISABLE KEYS */;
INSERT INTO `jlpt_examples` VALUES (1,1,'私はリンゴを食べます。','I eat an apple.','Basic present tense usage.'),(2,1,'昨日寿司を食べました。','I ate sushi yesterday.','Past tense example.'),(3,2,'あの犬はとても大きいです。','That dog is very big.','Adjective with desu.'),(4,2,'この家は大きいですが、古いです。','This house is big, but old.','Adjective + conjunction.'),(5,3,'彼は毎日学校に行きます。','He goes to school every day.','Daily routine.'),(6,3,'学校は8時に始まります。','School starts at 8 o\'clock.','Time expression.'),(25,14,'おにぎりを買います','Membeli onigiri','');
/*!40000 ALTER TABLE `jlpt_examples` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `jlpt_words`
--

LOCK TABLES `jlpt_words` WRITE;
/*!40000 ALTER TABLE `jlpt_words` DISABLE KEYS */;
INSERT INTO `jlpt_words` VALUES (1,'L01-1',1,'食べる','たべる','taberu','to eat','makan','DOSHI_ICHIDAN','Basic verb for eating.','2025-09-03 07:44:40'),(2,'L01-1',1,'大きい','おおきい','ookii','big, large','besar','KEIYOSHI','Common adjective.','2025-09-03 07:44:40'),(3,'L01-1',1,'学校','がっこう','gakkou','school','sekolah','MEISHI','Place for studying.','2025-09-03 07:44:40'),(14,'L01.2',1,'買う','かう','kau','Buy','Beli','DOSHI_ICHIDAN','kata kerja dari verb 1','2025-09-09 04:19:10');
/*!40000 ALTER TABLE `jlpt_words` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `lemma`
--

LOCK TABLES `lemma` WRITE;
/*!40000 ALTER TABLE `lemma` DISABLE KEYS */;
INSERT INTO `lemma` VALUES (9,'あう','会う','DOSHI_GODAN','う',NULL),(10,'たつ','立つ','DOSHI_GODAN','つ',NULL),(11,'とる','取る','DOSHI_GODAN','る',NULL),(12,'しぬ','死ぬ','DOSHI_GODAN','ぬ',NULL),(13,'あそぶ','遊ぶ','DOSHI_GODAN','ぶ',NULL),(14,'よむ','読む','DOSHI_GODAN','む',NULL),(15,'かく','書く','DOSHI_GODAN','く',NULL),(16,'およぐ','泳ぐ','DOSHI_GODAN','ぐ',NULL),(17,'はなす','話す','DOSHI_GODAN','す',NULL),(18,'たべる','食べる','DOSHI_ICHIDAN',NULL,NULL),(19,'ねる','寝る','DOSHI_ICHIDAN',NULL,NULL),(20,'おしえる','教える','DOSHI_ICHIDAN',NULL,NULL),(21,'みる','見る','DOSHI_ICHIDAN',NULL,NULL),(22,'おきる','起きる','DOSHI_ICHIDAN',NULL,NULL),(23,'かりる','借りる','DOSHI_ICHIDAN',NULL,NULL),(24,'する','する','DOSHI_IRREGULAR',NULL,NULL),(25,'べんきょうする','勉強する','DOSHI_IRREGULAR',NULL,NULL),(26,'そうじする','掃除する','DOSHI_IRREGULAR',NULL,NULL),(27,'くる','来る','DOSHI_IRREGULAR',NULL,NULL),(28,'もってくる','持って来る','DOSHI_IRREGULAR',NULL,NULL),(29,'あつい','暑い','KEIYOSHI',NULL,NULL),(30,'いい','良い','KEIYOSHI',NULL,NULL),(31,'ひま','暇','KEIYODOUSHI',NULL,NULL),(32,'あめ','雨','MEISHI',NULL,NULL);
/*!40000 ALTER TABLE `lemma` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `level`
--

LOCK TABLES `level` WRITE;
/*!40000 ALTER TABLE `level` DISABLE KEYS */;
INSERT INTO `level` VALUES (1,'N5','Level: Mampu memahami bahasa Jepang dasar. Target: ±100 kanji, ±800 kosakata. Nilai Minimal: Seksi (38 dari 120, 19 dari 60), Total (80 dari 180).'),(2,'N4','Level: Mampu memahami bahasa Jepang dasar, memahami percakapan sehari-hari yang diucapkan perlahan. Target: ±300 kanji, ±1.500 kosakata. Nilai Minimal: Seksi (38 dari 120, 19 dari 60), Total (90 dari 180).'),(3,'N3','Level: Mampu memahami bahasa Jepang yang digunakan dalam percakapan sehari-hari dengan kecepatan mendekati normal. Target: ±650 kanji, ±3.750 kosakata. Nilai Minimal: Seksi (19 dari 60), Total (95 dari 180).'),(4,'N2','Level: Mampu memahami bahasa Jepang yang digunakan dalam berbagai situasi sehari-hari dan profesional. Target: ±1.000 kanji, ±6.000 kosakata. Nilai Minimal: Seksi (19 dari 60), Total (90 dari 180).'),(5,'N1','Level: Mampu memahami bahasa Jepang yang digunakan dalam berbagai situasi, termasuk tulisan dengan topik yang kompleks. Target: ±2.000 kanji, ±10.000 kosakata. Nilai Minimal: Seksi (19 dari 60), Total (100 dari 180).');
/*!40000 ALTER TABLE `level` ENABLE KEYS */;
UNLOCK TABLES;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-09-16  4:47:29
