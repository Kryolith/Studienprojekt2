<?xml version="1.0" encoding="UTF-8"?>
<kml xmlns="http://earth.google.com/kml/2.1">
<Document>
<Style id="Poly1">
<LineStyle>
<color>7f00ff00</color>
<width>2</width>
</LineStyle>
<PolyStyle>
<color>7f00ff00</color>
</PolyStyle>
</Style>
<Placemark>
<name>{name}</name>
<description><img src='{name}.jpg' width = '400' /></description>
<styleUrl>#Poly1</styleUrl>
<Polygon>
<altitudeMode>clampToGround</altitudeMode>
<extrude>1</extrude>
<tessellate>1</tessellate>
<outerBoundaryIs>
<LinearRing>
<coordinates>
{loop:coordinates}
</coordinates>
</LinearRing>
</outerBoundaryIs>
</Polygon>
</Placemark>
</Document>
</kml>
