   M S B A M L   `   `   `  ����p  lSystem.Windows.Controls.Input.Toolkit, Version=3.5.40128.1, Culture=neutral, PublicKeyToken=31bf3856ad364e35pTclr-namespace:System.Windows.Controls;assembly=System.Windows.Controls.Input.ToolkitSystem.Windows.Controls  5      N JDecibelCSharpClient, Version=1.0.0.0, Culture=neutral, PublicKeyToken=nullI)clr-namespace:DecibelRestWpf.UserControlsDecibelRestWpf.UserControls 5      ,   &DecibelRestWpf.UserControls.WorkSearch   5      U QWPFToolkit, Version=3.5.40128.1, Culture=neutral, PublicKeyToken=31bf3856ad364e35^ ZWindowsFormsIntegration, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35H 9http://schemas.microsoft.com/winfx/2006/xaml/presentation      5      R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5      Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     5      @d2http://schemas.microsoft.com/expression/blend/2008     5      cmyTclr-namespace:System.Windows.Controls;assembly=System.Windows.Controls.Input.Toolkit     5      7c)clr-namespace:DecibelRestWpf.UserControls     5      .��5	      �   � 	Resources  5
   
    �� MergedDictionaries �� Source�� 5       5      �� 5      $" ../Resources/DatePicker.xaml?�6)   �� 5      $% ../Resources/SearchTabItem.xaml?�6)   %    5       � ColumnDefinitions 5   
   �� 5      $��80 �6   �� 5      $	��285 �6   �� 5      $��70 �6   �� 5      $	��285 �6   �� 5      $
��Auto �6   �� 5       � RowDefinitions 5   
   �� 5      $��28 �6   �� 5      $��28 �6   �� 5      $��28 �6   �� 5       $��28 �6   �� 5!      $��22 �6   �� 5"      $��22 �6   �� 5#      $��* �6   .+�5'   
   �� -   5        $��lblSearchParameters��5'   �      ControlBaseStyle#��[  6   $��Search Parameters��6:   $��0��6V   $��0��6f   $��4��6s   6�   �� 5)   
   -   5        $��lblSearchName��5)   V   #��[  6   $
��Name��6:   $��1��6I   6V   -   'System.Windows.Controls.AutoCompleteBox  5*   
   -   5        $��txtSearchName��5*   q     AutoCompleteStyle#��[ 6   $��1��6H   $��1��6X   $��1q�6e   6�   �� 5,   
   -   5        $��lblSearchCatalogue��5,   k   #��[  6   $��	Catalogue��6:   $��0��6N   $��2��6^   6k     5-   
   -   5        $��txtSearchCatalogue��5-   e   #��[ 6   $��1��6H   $��2��6X   6�   �� 5/   
   -   5        $��lblSearchComposer��5/   j   #��[  6   $��Composer��6:   $��0��6M   $��3��6]   6j     50   
   -   5        $��txtSearchComposers��50   e   #��[ 6   $��1��6H   $��3��6X   6�   �� 52   
   -   5        $��lblSearchDepth��52   g   #��[  6   $��Depth��6:   $��2��6J   $��1��6Z   6g   �� 53   
   -	   5        $��txtSearchDepth��53   U     TextBoxStyle#��[ 6   $��3��68   $��1��6H    �� 
IsReadOnly . 6�   �� 54   
   -
   5        $��btnSelectDepth��54   t   $	��...��6   $��4��6   $��1��6/   $��23��6<   $
��Left=�6H   $��0,2,0,0q�6c   $	��Top=�6�   $��25��6�   6�   �� 56   
   -   5        $��lbSearchlURL��56   v   #��[  6   $	��URL��6:   $��0��6H   $��4��6X   $��2��6e   6v   �� 57   
   -   5        $��txtSearchUrl��57   �   #��[ 6   $��1��68   $��4��6H   $��3��6U   $��2��6i   $	��615��6z   $��40��6�    .  �� TextWrapping$
 Wrap=�6�   " �� VerticalScrollBarVisibility$
 Auto=�6�   �� 5:   
   -   5        $��tbcResponse��5:   F   $��6��6   $��0��6)   $��6��69   .@�5;      �� -   5        $��
tabResults��5;   '   $��Results��6   .��5<      �  5=      �� 5>      $��* �6+    5A      �� 5B      $��* �6(   �� 5C      $	��258 �6(   .+�5G      &   System.Windows.Controls.DataGrid  -   5        $��dtgWorkResults��5G   �      AutoGenerateColumns .  	   SelectionMode$	 Single=�6;   $��0��6R   $��2��6b   $��0��6v   $��Stretch=�6�   $��Stretch=�6�   $
��Auto��6�   $
��Auto��6�   
   Columns
 5H      0  *System.Windows.Controls.DataGridTextColumn  5I      ,  &System.Windows.Controls.DataGridColumn   Header$ Index��61      Width5  /System.Windows.Controls.DataGridLengthConverter$ 50 6@   1  +System.Windows.Controls.DataGridBoundColumn   Binding 6K   �� *Index+  5J      $
 Name��61   $	 460 6?    6K   �� *Name+  5K      $ 	Composers��61   $	 455 6D    6Q   �� *	Composers+5L      �� 5O      -   5        $��tbcAlbumInformation��5O   W     TabControlStyle#��[ 6!   $��1��6J   .@�5P      �� -   5        $��tabTrackAppearances��5P   =   $��Track Appearances��6"   .��5Q        -   5        $��dtgTrackAppearances��5Q   �    .  $	 Single=�6C   $��0��6Z   $��2��6j   $��0��6~   $��Stretch=�6�   $��Stretch=�6�   $
��Auto��6�   $
��Auto��6�   
 5R   "     5S   &   $ Index��69   $ 50 6H    6S   �� *Index+  5T   &   $ Track��69   $	 235 6H    6T   �� *	TrackName+  5U   &   $ Artists��69   $	 235 6J    6W   �� *
ArtistName+  5V   &   $ Album��69   $	 235 6H    6T   �� *	AlbumName+  5W   &   $ Track #��69   $	 100 6J    6V   �� *TrackNumber+  5X   &   $ Length��69   $	 100 6I    6U   �� *TrackLength+5Z      5[      5\      5]      5^      �� 5a      -   5        $��tabResponse��5a   #   $	��XML��6   .��5b      �  5c      �� 5d      $��* �6+    5g      �� 5h      $��* �6(   .+�5k      �� -   5        $��txtResponse��5k   Y   $��1��6   $��2��6.   $��Stretch=�6;   $��Stretch=�6l    �� AcceptsReturn . $
 Auto=�6�   $ �� HorizontalScrollBarVisibility$
 Auto=�6�    . 6�   5l      5m      5n      �� 5q   
   -   5        $��	btnSearch��5q   f   $��Search��6   $��3��6"   $��4��62   $��23��6?   $
��Left=�6K   $	��Top=�6w   $��88��6�   $��
284,19,0,0q�6�   $��2��6�    �� 	IsDefault . $��3��6�   �� 5t   
   $��1��6   $��5��6%   $��2��62   $��22��6C   $��Right=�6O   $��0,21,0,0q�6k   $	��Top=�6}   $	��934��6�   $��5��6�   .+�5u      /  )DecibelRestWpf.UserControls.PageNavigator  -   5          Name$ pageNavigator��5u      $��Right=�65   $	��156��6Q   5v      5w      5x      