   M S B A M L   `   `   `  ����p  lSystem.Windows.Controls.Input.Toolkit, Version=3.5.40128.1, Culture=neutral, PublicKeyToken=31bf3856ad364e35pTclr-namespace:System.Windows.Controls;assembly=System.Windows.Controls.Input.ToolkitSystem.Windows.Controls  5      N JDecibelCSharpClient, Version=1.0.0.0, Culture=neutral, PublicKeyToken=nullI)clr-namespace:DecibelRestWpf.UserControlsDecibelRestWpf.UserControls 5      -   'DecibelRestWpf.UserControls.TrackSearch   5      U QWPFToolkit, Version=3.5.40128.1, Culture=neutral, PublicKeyToken=31bf3856ad364e35^ ZWindowsFormsIntegration, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35H 9http://schemas.microsoft.com/winfx/2006/xaml/presentation      5      R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5      Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     5      @d2http://schemas.microsoft.com/expression/blend/2008     5      cmyTclr-namespace:System.Windows.Controls;assembly=System.Windows.Controls.Input.Toolkit     5      7c)clr-namespace:DecibelRestWpf.UserControls     5      .��5	      �   � 	Resources  5
   
    �� MergedDictionaries �� Source�� 5       5      �� 5      $" ../Resources/DatePicker.xaml?�6)   �� 5      $% ../Resources/SearchTabItem.xaml?�6)   %    5       � ColumnDefinitions 5   
   �� 5      $��80 �6   �� 5      $	��285 �6   �� 5      $��70 �6   �� 5      $	��285 �6   �� 5      $
��Auto �6   �� 5       � RowDefinitions 5   
   �� 5      $��28 �6   �� 5      $��28 �6   �� 5      $��28 �6   �� 5       $��28 �6   �� 5!      $��22 �6   �� 5"      $��22 �6   �� 5#      $��* �6   .+�5'   
   �� -   5        $��lblSearchParameters��5'   �      ControlBaseStyle#��[  6   $��Search Parameters��6:   $��0��6V   $��0��6f   $��4��6s   6�   �� 5)   
   -   5        $��lblSearchTrackTitle��5)   m   #��[  6   $��Track Title��6:   $��0��6P   $��1��6`   6m   -   'System.Windows.Controls.AutoCompleteBox  5*   
   -   5        $��txtSearchTrackTitle��5*   q     AutoCompleteStyle#��[ 6   $��1��6H   $��1��6X   $��1q�6e   6�   �� 5,   
   -   5        $��lblSearchArtist��5,   h   #��[  6   $��Artist��6:   $��0��6K   $��2��6[   6h     5-   
   -   5        $��txtSearchArtist��5-   e   #��[ 6   $��1��6H   $��2��6X   6�   �� 5/   
   -   5        $��lblSearchAlbumTitle��5/   m   #��[  6   $��Album Title��6:   $��0��6P   $��3��6`   6m     50   
   -   5        $��txtSearchAlbumTitle��50   e   #��[ 6   $��1��6H   $��3��6X   6�   �� 52   
   -   5        $��lblSearchDepth��52   g   #��[  6   $��Depth��6:   $��2��6J   $��1��6Z   6g   �� 53   
   -	   5        $��txtSearchDepth��53   U     TextBoxStyle#��[ 6   $��3��68   $��1��6H    �� 
IsReadOnly . 6k   �� 54   
   -
   5        $��btnSelectDepth��54   t   $	��...��6   $��4��6   $��1��6/   $��23��6<   $
��Left=�6H   $��0,2,0,0q�6c   $	��Top=�6�   $��25��6�   6�   �� 56   
   -   5        $��lbSearchlURL��56   v   #��[  6   $	��URL��6:   $��0��6H   $��4��6X   $��2��6e   6v   �� 57   
   -   5        $��txtSearchUrl��57   �   #��[ 6   $��1��68   $��4��6H   $��3��6U   $��2��6i   $	��615��6z   $��40��6�    .  �� TextWrapping$
 Wrap=�6�   " �� VerticalScrollBarVisibility$
 Auto=�6�   �� 5:   
   -   5        $��tbcResponse��5:   F   $��6��6   $��0��6)   $��6��69   .@�5;      �� -   5        $��
tabResults��5;   '   $��Results��6   .��5<      �  5=      �� 5>      $	��485 �6+   �� 5?      $��* �6+    5B      �� 5C      $��* �6(   �� 5D      $	��258 �6(   .+�5H      &   System.Windows.Controls.DataGrid  -   5        $��dtgTrackResults��5H   �      AutoGenerateColumns .  $��0��6;   $��2��6K   $��0��6_   $��Stretch=�6l   $��Stretch=�6�   $
��Auto��6�   $
��Auto��6�   	   Columns	 5I      0  *System.Windows.Controls.DataGridTextColumn  5J      ,  &System.Windows.Controls.DataGridColumn
   Header$
 Index��61      Width5  /System.Windows.Controls.DataGridLengthConverter$ 50 6@   1  +System.Windows.Controls.DataGridBoundColumn   Binding 6K   �� *Index+  5K      $

 Name��61   $	 355 6?    6K   �� *Name+  5L      $
 Artists��61   $	 355 6B    6N   �� *	Artists+  5M      $
 Track #��61   $ 80 6B    6N   �� *SequenceNumber+  5N      $
 Track Length��61   $	 125 6G    6S   �� *TrackLength+5P      �� 5S      -   5        $��tbcAlbumInformation��5S   W     TabControlStyle#��[ 6!   $��1��6J   .@�5T      �� -   5        $��tabAlbumInformation��5T   =   $��Album Information��6"   .��5U      2  ,DecibelRestWpf.UserControls.AlbumInformation  -   5          Name$ albumInformation��5U   1   5V      5W      �� 5Z      -   5        $��tbcTrackInformation��5Z   x   #��[ 6!   $��1��6J   $��1��6Z   $��2,2,0,0q�6g   .@�5[      �� -   5        $��tabTrackInformation��5[   9   $��Track Listing��6"   .��5\      � .+�5]   "   �� -   5        $��	lstTracks��5]   Y   $��Stretch=�6*   $��0,0,0,0q�6H   $��Stretch=�6j   $
��Auto��6�   6  5^      5_      5`      5a      5b      �� 5e      -   5        $��tabResponse��5e   #   $	��XML��6   .��5f      �  5g      �� 5h      $��* �6+    5k      �� 5l      $��* �6(   .+�5o      �� -   5        $��txtResponse��5o   Y   $��1��6   $��2��6.   $��Stretch=�6;   $��Stretch=�6l    �� AcceptsReturn . $
 Auto=�6�   $ �� HorizontalScrollBarVisibility$
 Auto=�6�    . 6�   5p      5q      5r      �� 5u   
   -   5        $��	btnSearch��5u   f   $��Search��6   $��3��6"   $��4��62   $��23��6?   $
��Left=�6K   $	��Top=�6w   $��88��6�   $��
284,19,0,0q�6�   $��2��6�    �� 	IsDefault . $��3��6�   6�   �� 5x   
   $��1��6   $��5��6%   $��2��62   $��22��6C   $��Right=�6O   $��0,21,0,0q�6k   $	��Top=�6}   $	��934��6�   $��5��6�   .+�5y      /  )DecibelRestWpf.UserControls.PageNavigator  -   5          Name$ pageNavigator��5y      $��Right=�65   $	��156��6Q   5z      5{      5|      