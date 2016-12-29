# FontStats
Font Statistics

This program generates various statistics for the fonts installed on a computer.

These statistics include:
- weight of the font
- various metrics for width and height
- the fonts can be normalized (set a font size) to have similar widths or heights, before computing the other metrics

Samples using these fonts are then displayed in a table and the samples can be sorted using the computed statistics/metrics.

TODO:
- add more statistics / metrics: e.g. median/quantiles for the different metrics
- option to process multiple lines of sample text: (String [] or auto-split after some specific length)
- option to use Font kerning
- optimize font normalization and add more options (if useful)
