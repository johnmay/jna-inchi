/**
 * JNA-InChI - Library for calling InChI from Java
 * Copyright © 2018 Daniel Lowe
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.dan2097.jnainchi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class InchiOptionsTest {

  @Test
  public void testFixedHydrogenLayer() throws IOException {
    String tautomer1 = "N1=CN=C2N=CNC2=C1";
    String tautomer2 = "N1=CN=C2NC=NC2=C1";
    
    assertEquals("InChI=1S/C5H4N4/c1-4-5(8-2-6-1)9-3-7-4/h1-3H,(H,6,7,8,9)", SmilesToInchi.toInchi(tautomer1).getInchi());
    assertEquals("InChI=1S/C5H4N4/c1-4-5(8-2-6-1)9-3-7-4/h1-3H,(H,6,7,8,9)", SmilesToInchi.toInchi(tautomer2).getInchi());
    
    InchiOptions options = new InchiOptions.InchiOptionsBuilder().withFlag(InchiFlag.FixedH).build();
    assertEquals("InChI=1/C5H4N4/c1-4-5(8-2-6-1)9-3-7-4/h1-3H,(H,6,7,8,9)/f/h7H", SmilesToInchi.toInchi(tautomer1, options).getInchi());
    assertEquals("InChI=1/C5H4N4/c1-4-5(8-2-6-1)9-3-7-4/h1-3H,(H,6,7,8,9)/f/h9H", SmilesToInchi.toInchi(tautomer2, options).getInchi());
  }
  
  @Test
  public void testReconnectedMetalLayer() throws IOException {
    String metalComplex = "[NH3][Pt](Cl)(Cl)[NH3]";
    assertEquals("InChI=1S/2ClH.2H3N.Pt/h2*1H;2*1H3;/q;;;;+2/p-2", SmilesToInchi.toInchi(metalComplex).getInchi());

    InchiOptions options = new InchiOptions.InchiOptionsBuilder().withFlag(InchiFlag.RecMet).build();
    assertEquals("InChI=1/2ClH.2H3N.Pt/h2*1H;2*1H3;/q;;;;+2/p-2/rCl2H6N2Pt/c1-5(2,3)4/h3-4H3", SmilesToInchi.toInchi(metalComplex, options).getInchi());
  }
  
  @Test
  public void testWildcardAtoms() throws IOException {
    String ethylFragment = "CC*";
    InchiOptions options1 = new InchiOptions.InchiOptionsBuilder().withFlag(InchiFlag.NPZz).build();
    assertEquals("InChI=1B/C2H5Zz/c1-2-3/h2H2,1H3", SmilesToInchi.toInchi(ethylFragment, options1).getInchi());
    
    String stereoExample = "Cl[C@@H](Br)*";
    InchiOptions options = new InchiOptions.InchiOptionsBuilder().withFlag(InchiFlag.NPZz, InchiFlag.SAtZz).build();
    assertEquals("InChI=1B/CHBrClZz/c2-1(3)4/h1H/t1-/m1/s1", SmilesToInchi.toInchi(stereoExample, options).getInchi());
  }
  
  @Test
  public void testRelStereoFlag() throws IOException {
    InchiOptions options = new InchiOptions.InchiOptionsBuilder().withFlag(InchiFlag.SRel).build();
    assertEquals("InChI=1/C8H17N/c1-3-8-6-7(2)4-5-9-8/h7-9H,3-6H2,1-2H3/t7-,8+/s2", SmilesToInchi.toInchi("C[C@@H]1C[C@H](NCC1)CC", options).getInchi());
  }

  @Test
  public void testRacStereoFlag() throws IOException {
    InchiOptions options = new InchiOptions.InchiOptionsBuilder().withFlag(InchiFlag.SRac).build();
    assertEquals("InChI=1/C3H7NO2/c1-2(4)3(5)6/h2H,4H2,1H3,(H,5,6)/t2-/s3", SmilesToInchi.toInchi("N[C@@H](C)C(=O)O", options).getInchi());
  }
  
  @Test
  public void testIgnoreStereoFlag() throws IOException {
    InchiOptions options = new InchiOptions.InchiOptionsBuilder().withFlag(InchiFlag.SNon).build();
    assertEquals("InChI=1S/C3H7NO2/c1-2(4)3(5)6/h2H,4H2,1H3,(H,5,6)", SmilesToInchi.toInchi("N[C@@H](C)C(=O)O", options).getInchi());
  }
  
  @Test
  public void testIndicateUndefinedStereoFlag() throws IOException {
    InchiOptions options = new InchiOptions.InchiOptionsBuilder().withFlag(InchiFlag.SUU).build();
    assertEquals("InChI=1/C3H7NO2/c1-2(4)3(5)6/h2H,4H2,1H3,(H,5,6)/t2?", SmilesToInchi.toInchi("NC(C)C(=O)O", options).getInchi());
  }

  @Test
  public void testLargeMoleculeSupport() throws IOException {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 2000; i++) {
      sb.append('C');
    }
    String smiles = sb.toString();
    InchiOutput naiveResult = SmilesToInchi.toInchi(smiles);
    assertNull(naiveResult.getInchi());
    assertEquals(InchiStatus.ERROR, naiveResult.getStatus());

    InchiOptions options = new InchiOptions.InchiOptionsBuilder().withFlag(InchiFlag.LargeMolecules).build();
    InchiOutput result = SmilesToInchi.toInchi(smiles, options);
    assertEquals("InChI=1B/C2000H4002/c1-3-5-7-9-11-13-15-17-19-21-23-25-27-29-31-33-35-37-39-41-43-45-47-49-51-53-55-57-59-61-63-65-67-69-71-73-75-77-79-81-83-85-87-89-91-93-95-97-99-101-103-105-107-109-111-113-115-117-119-121-123-125-127-129-131-133-135-137-139-141-143-145-147-149-151-153-155-157-159-161-163-165-167-169-171-173-175-177-179-181-183-185-187-189-191-193-195-197-199-201-203-205-207-209-211-213-215-217-219-221-223-225-227-229-231-233-235-237-239-241-243-245-247-249-251-253-255-257-259-261-263-265-267-269-271-273-275-277-279-281-283-285-287-289-291-293-295-297-299-301-303-305-307-309-311-313-315-317-319-321-323-325-327-329-331-333-335-337-339-341-343-345-347-349-351-353-355-357-359-361-363-365-367-369-371-373-375-377-379-381-383-385-387-389-391-393-395-397-399-401-403-405-407-409-411-413-415-417-419-421-423-425-427-429-431-433-435-437-439-441-443-445-447-449-451-453-455-457-459-461-463-465-467-469-471-473-475-477-479-481-483-485-487-489-491-493-495-497-499-501-503-505-507-509-511-513-515-517-519-521-523-525-527-529-531-533-535-537-539-541-543-545-547-549-551-553-555-557-559-561-563-565-567-569-571-573-575-577-579-581-583-585-587-589-591-593-595-597-599-601-603-605-607-609-611-613-615-617-619-621-623-625-627-629-631-633-635-637-639-641-643-645-647-649-651-653-655-657-659-661-663-665-667-669-671-673-675-677-679-681-683-685-687-689-691-693-695-697-699-701-703-705-707-709-711-713-715-717-719-721-723-725-727-729-731-733-735-737-739-741-743-745-747-749-751-753-755-757-759-761-763-765-767-769-771-773-775-777-779-781-783-785-787-789-791-793-795-797-799-801-803-805-807-809-811-813-815-817-819-821-823-825-827-829-831-833-835-837-839-841-843-845-847-849-851-853-855-857-859-861-863-865-867-869-871-873-875-877-879-881-883-885-887-889-891-893-895-897-899-901-903-905-907-909-911-913-915-917-919-921-923-925-927-929-931-933-935-937-939-941-943-945-947-949-951-953-955-957-959-961-963-965-967-969-971-973-975-977-979-981-983-985-987-989-991-993-995-997-999-1001-1003-1005-1007-1009-1011-1013-1015-1017-1019-1021-1023-1025-1027-1029-1031-1033-1035-1037-1039-1041-1043-1045-1047-1049-1051-1053-1055-1057-1059-1061-1063-1065-1067-1069-1071-1073-1075-1077-1079-1081-1083-1085-1087-1089-1091-1093-1095-1097-1099-1101-1103-1105-1107-1109-1111-1113-1115-1117-1119-1121-1123-1125-1127-1129-1131-1133-1135-1137-1139-1141-1143-1145-1147-1149-1151-1153-1155-1157-1159-1161-1163-1165-1167-1169-1171-1173-1175-1177-1179-1181-1183-1185-1187-1189-1191-1193-1195-1197-1199-1201-1203-1205-1207-1209-1211-1213-1215-1217-1219-1221-1223-1225-1227-1229-1231-1233-1235-1237-1239-1241-1243-1245-1247-1249-1251-1253-1255-1257-1259-1261-1263-1265-1267-1269-1271-1273-1275-1277-1279-1281-1283-1285-1287-1289-1291-1293-1295-1297-1299-1301-1303-1305-1307-1309-1311-1313-1315-1317-1319-1321-1323-1325-1327-1329-1331-1333-1335-1337-1339-1341-1343-1345-1347-1349-1351-1353-1355-1357-1359-1361-1363-1365-1367-1369-1371-1373-1375-1377-1379-1381-1383-1385-1387-1389-1391-1393-1395-1397-1399-1401-1403-1405-1407-1409-1411-1413-1415-1417-1419-1421-1423-1425-1427-1429-1431-1433-1435-1437-1439-1441-1443-1445-1447-1449-1451-1453-1455-1457-1459-1461-1463-1465-1467-1469-1471-1473-1475-1477-1479-1481-1483-1485-1487-1489-1491-1493-1495-1497-1499-1501-1503-1505-1507-1509-1511-1513-1515-1517-1519-1521-1523-1525-1527-1529-1531-1533-1535-1537-1539-1541-1543-1545-1547-1549-1551-1553-1555-1557-1559-1561-1563-1565-1567-1569-1571-1573-1575-1577-1579-1581-1583-1585-1587-1589-1591-1593-1595-1597-1599-1601-1603-1605-1607-1609-1611-1613-1615-1617-1619-1621-1623-1625-1627-1629-1631-1633-1635-1637-1639-1641-1643-1645-1647-1649-1651-1653-1655-1657-1659-1661-1663-1665-1667-1669-1671-1673-1675-1677-1679-1681-1683-1685-1687-1689-1691-1693-1695-1697-1699-1701-1703-1705-1707-1709-1711-1713-1715-1717-1719-1721-1723-1725-1727-1729-1731-1733-1735-1737-1739-1741-1743-1745-1747-1749-1751-1753-1755-1757-1759-1761-1763-1765-1767-1769-1771-1773-1775-1777-1779-1781-1783-1785-1787-1789-1791-1793-1795-1797-1799-1801-1803-1805-1807-1809-1811-1813-1815-1817-1819-1821-1823-1825-1827-1829-1831-1833-1835-1837-1839-1841-1843-1845-1847-1849-1851-1853-1855-1857-1859-1861-1863-1865-1867-1869-1871-1873-1875-1877-1879-1881-1883-1885-1887-1889-1891-1893-1895-1897-1899-1901-1903-1905-1907-1909-1911-1913-1915-1917-1919-1921-1923-1925-1927-1929-1931-1933-1935-1937-1939-1941-1943-1945-1947-1949-1951-1953-1955-1957-1959-1961-1963-1965-1967-1969-1971-1973-1975-1977-1979-1981-1983-1985-1987-1989-1991-1993-1995-1997-1999-2000-1998-1996-1994-1992-1990-1988-1986-1984-1982-1980-1978-1976-1974-1972-1970-1968-1966-1964-1962-1960-1958-1956-1954-1952-1950-1948-1946-1944-1942-1940-1938-1936-1934-1932-1930-1928-1926-1924-1922-1920-1918-1916-1914-1912-1910-1908-1906-1904-1902-1900-1898-1896-1894-1892-1890-1888-1886-1884-1882-1880-1878-1876-1874-1872-1870-1868-1866-1864-1862-1860-1858-1856-1854-1852-1850-1848-1846-1844-1842-1840-1838-1836-1834-1832-1830-1828-1826-1824-1822-1820-1818-1816-1814-1812-1810-1808-1806-1804-1802-1800-1798-1796-1794-1792-1790-1788-1786-1784-1782-1780-1778-1776-1774-1772-1770-1768-1766-1764-1762-1760-1758-1756-1754-1752-1750-1748-1746-1744-1742-1740-1738-1736-1734-1732-1730-1728-1726-1724-1722-1720-1718-1716-1714-1712-1710-1708-1706-1704-1702-1700-1698-1696-1694-1692-1690-1688-1686-1684-1682-1680-1678-1676-1674-1672-1670-1668-1666-1664-1662-1660-1658-1656-1654-1652-1650-1648-1646-1644-1642-1640-1638-1636-1634-1632-1630-1628-1626-1624-1622-1620-1618-1616-1614-1612-1610-1608-1606-1604-1602-1600-1598-1596-1594-1592-1590-1588-1586-1584-1582-1580-1578-1576-1574-1572-1570-1568-1566-1564-1562-1560-1558-1556-1554-1552-1550-1548-1546-1544-1542-1540-1538-1536-1534-1532-1530-1528-1526-1524-1522-1520-1518-1516-1514-1512-1510-1508-1506-1504-1502-1500-1498-1496-1494-1492-1490-1488-1486-1484-1482-1480-1478-1476-1474-1472-1470-1468-1466-1464-1462-1460-1458-1456-1454-1452-1450-1448-1446-1444-1442-1440-1438-1436-1434-1432-1430-1428-1426-1424-1422-1420-1418-1416-1414-1412-1410-1408-1406-1404-1402-1400-1398-1396-1394-1392-1390-1388-1386-1384-1382-1380-1378-1376-1374-1372-1370-1368-1366-1364-1362-1360-1358-1356-1354-1352-1350-1348-1346-1344-1342-1340-1338-1336-1334-1332-1330-1328-1326-1324-1322-1320-1318-1316-1314-1312-1310-1308-1306-1304-1302-1300-1298-1296-1294-1292-1290-1288-1286-1284-1282-1280-1278-1276-1274-1272-1270-1268-1266-1264-1262-1260-1258-1256-1254-1252-1250-1248-1246-1244-1242-1240-1238-1236-1234-1232-1230-1228-1226-1224-1222-1220-1218-1216-1214-1212-1210-1208-1206-1204-1202-1200-1198-1196-1194-1192-1190-1188-1186-1184-1182-1180-1178-1176-1174-1172-1170-1168-1166-1164-1162-1160-1158-1156-1154-1152-1150-1148-1146-1144-1142-1140-1138-1136-1134-1132-1130-1128-1126-1124-1122-1120-1118-1116-1114-1112-1110-1108-1106-1104-1102-1100-1098-1096-1094-1092-1090-1088-1086-1084-1082-1080-1078-1076-1074-1072-1070-1068-1066-1064-1062-1060-1058-1056-1054-1052-1050-1048-1046-1044-1042-1040-1038-1036-1034-1032-1030-1028-1026-1024-1022-1020-1018-1016-1014-1012-1010-1008-1006-1004-1002-1000-998-996-994-992-990-988-986-984-982-980-978-976-974-972-970-968-966-964-962-960-958-956-954-952-950-948-946-944-942-940-938-936-934-932-930-928-926-924-922-920-918-916-914-912-910-908-906-904-902-900-898-896-894-892-890-888-886-884-882-880-878-876-874-872-870-868-866-864-862-860-858-856-854-852-850-848-846-844-842-840-838-836-834-832-830-828-826-824-822-820-818-816-814-812-810-808-806-804-802-800-798-796-794-792-790-788-786-784-782-780-778-776-774-772-770-768-766-764-762-760-758-756-754-752-750-748-746-744-742-740-738-736-734-732-730-728-726-724-722-720-718-716-714-712-710-708-706-704-702-700-698-696-694-692-690-688-686-684-682-680-678-676-674-672-670-668-666-664-662-660-658-656-654-652-650-648-646-644-642-640-638-636-634-632-630-628-626-624-622-620-618-616-614-612-610-608-606-604-602-600-598-596-594-592-590-588-586-584-582-580-578-576-574-572-570-568-566-564-562-560-558-556-554-552-550-548-546-544-542-540-538-536-534-532-530-528-526-524-522-520-518-516-514-512-510-508-506-504-502-500-498-496-494-492-490-488-486-484-482-480-478-476-474-472-470-468-466-464-462-460-458-456-454-452-450-448-446-444-442-440-438-436-434-432-430-428-426-424-422-420-418-416-414-412-410-408-406-404-402-400-398-396-394-392-390-388-386-384-382-380-378-376-374-372-370-368-366-364-362-360-358-356-354-352-350-348-346-344-342-340-338-336-334-332-330-328-326-324-322-320-318-316-314-312-310-308-306-304-302-300-298-296-294-292-290-288-286-284-282-280-278-276-274-272-270-268-266-264-262-260-258-256-254-252-250-248-246-244-242-240-238-236-234-232-230-228-226-224-222-220-218-216-214-212-210-208-206-204-202-200-198-196-194-192-190-188-186-184-182-180-178-176-174-172-170-168-166-164-162-160-158-156-154-152-150-148-146-144-142-140-138-136-134-132-130-128-126-124-122-120-118-116-114-112-110-108-106-104-102-100-98-96-94-92-90-88-86-84-82-80-78-76-74-72-70-68-66-64-62-60-58-56-54-52-50-48-46-44-42-40-38-36-34-32-30-28-26-24-22-20-18-16-14-12-10-8-6-4-2/h3-2000H2,1-2H3", result.getInchi());
    assertEquals(InchiStatus.SUCCESS, result.getStatus());
  }
  
  @Test
  public void testKetoEnol() throws IOException {
    String tautomer1 = "CC(=O)CC(=O)C";
    String tautomer2 = "CC(O)=CC(=O)C";
    
    assertEquals("InChI=1S/C5H8O2/c1-4(6)3-5(2)7/h3H2,1-2H3", SmilesToInchi.toInchi(tautomer1).getInchi());
    assertEquals("InChI=1S/C5H8O2/c1-4(6)3-5(2)7/h3,6H,1-2H3", SmilesToInchi.toInchi(tautomer2).getInchi());
    
    InchiOptions options = new InchiOptions.InchiOptionsBuilder().withFlag(InchiFlag.KET).build();
    assertEquals("InChI=1/C5H8O2/c1-4(6)3-5(2)7/h1-2H3,(H2,3,6,7)", SmilesToInchi.toInchi(tautomer1, options).getInchi());
    assertEquals("InChI=1/C5H8O2/c1-4(6)3-5(2)7/h1-2H3,(H2,3,6,7)", SmilesToInchi.toInchi(tautomer2, options).getInchi());
  }

  @Test
  public void test_1_5_tautomerism() throws IOException {
    String tautomer1 = "N=CC=CNC";
    String tautomer2 = "NC=CC=NC";
    
    assertEquals("InChI=1S/C4H8N2/c1-6-4-2-3-5/h2-6H,1H3", SmilesToInchi.toInchi(tautomer1).getInchi());
    assertEquals("InChI=1S/C4H8N2/c1-6-4-2-3-5/h2-4H,5H2,1H3", SmilesToInchi.toInchi(tautomer2).getInchi());
    
    InchiOptions options = new InchiOptions.InchiOptionsBuilder().withFlag(InchiFlag.OneFiveT).build();
    assertEquals("InChI=1/C4H8N2/c1-6-4-2-3-5/h2-4H,1H3,(H2,5,6)", SmilesToInchi.toInchi(tautomer1, options).getInchi());
    assertEquals("InChI=1/C4H8N2/c1-6-4-2-3-5/h2-4H,1H3,(H2,5,6)", SmilesToInchi.toInchi(tautomer2, options).getInchi());
  }
  
  @Test
  public void testAuxNone() throws IOException {
    assertNotNull(SmilesToInchi.toInchi("C").getAuxInfo());
    
    //disabling auxillary info offers a small performance improvement
    InchiOptions options = new InchiOptions.InchiOptionsBuilder().withFlag(InchiFlag.AuxNone).build();
    assertNull(SmilesToInchi.toInchi("C", options).getAuxInfo());
  }

  @Test
  @Disabled("Might be an InChI library bug")
  public void testWarnOnEmptyStructure() throws IOException {
    InchiOutput result1 = SmilesToInchi.toInchi("");
    assertEquals(InchiStatus.ERROR, result1.getStatus());
    assertNull(result1.getInchi());
    InchiOptions options = new InchiOptions.InchiOptionsBuilder().withFlag(InchiFlag.WarnOnEmptyStructure).build();
    InchiOutput result2 = SmilesToInchi.toInchi("", options);
    assertEquals(InchiStatus.WARNING, result2.getStatus());
    assertEquals("InChI=1S//", result2.getInchi());
  }
  
  @Test
  public void testEmptyInchiOnError() throws IOException {
    String smiles = "CC*";
    InchiOutput result1 = SmilesToInchi.toInchi(smiles);
    assertEquals(InchiStatus.ERROR, result1.getStatus());
    assertNull(result1.getInchi());
    InchiOptions options = new InchiOptions.InchiOptionsBuilder().withFlag(InchiFlag.OutErrInChI).build();
    InchiOutput result2 = SmilesToInchi.toInchi(smiles, options);
    assertEquals(InchiStatus.ERROR, result2.getStatus());
    assertEquals("InChI=1S//", result2.getInchi());
  }

}
