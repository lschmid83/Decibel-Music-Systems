   M S B A M L   `   `   `  ����p  lSystem.Windows.Controls.Input.Toolkit, Version=3.5.40128.1, Culture=neutral, PublicKeyToken=31bf3856ad364e35pTclr-namespace:System.Windows.Controls;assembly=System.Windows.Controls.Input.ToolkitSystem.Windows.Controls  5      N JDecibelCSharpClient, Version=1.0.0.0, Culture=neutral, PublicKeyToken=nullI)clr-namespace:DecibelRestWpf.UserControlsDecibelRestWpf.UserControls 5      3   -DecibelRestWpf.UserControls.ParticipantSearch   5      U QWPFToolkit, Version=3.5.40128.1, Culture=neutral, PublicKeyToken=31bf3856ad364e35^ ZWindowsFormsIntegration, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35H 9http://schemas.microsoft.com/winfx/2006/xaml/presentation      5      R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5      Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     5      @d2http://schemas.microsoft.com/expression/blend/2008     5      cmyTclr-namespace:System.Windows.Controls;assembly=System.Windows.Controls.Input.Toolkit     5      7c)clr-namespace:DecibelRestWpf.UserControls     5      .��5	      �   � 	Resources  5
   
    �� MergedDictionaries �� Source�� 5       5      �� 5      $" ../Resources/DatePicker.xaml?�6)   �� 5      $% ../Resources/SearchTabItem.xaml?�6)   %    5       � ColumnDefinitions 5   
   �� 5      $��80 �6   �� 5      $	��285 �6   �� 5      $��70 �6   �� 5      $	��285 �6   �� 5      $
��Auto �6   �� 5       � RowDefinitions 5   
   �� 5      $��28 �6   �� 5      $��28 �6   �� 5      $��28 �6   �� 5       $��28 �6   �� 5!      $��22 �6   �� 5"      $��22 �6   �� 5#      $��* �6   .+�5'   
   �� -   5        $��lblSearchParameters��5'   �      ControlBaseStyle#��[  6   $��Search Parameters��6:   $��0��6V   $��0��6f   $��4��6s   6�   �� 5)   
   -   5        $��lblSearchName��5)   f   #��[  6   $
��Name��6:   $��0��6I   $��1��6Y   6f   -   'System.Windows.Controls.AutoCompleteBox  5*   
   -   5        $��txtSearchName��5*   q     AutoCompleteStyle#��[ 6   $��1��6H   $��1��6X   $��1q�6e   6�   �� 5,   
   -   5        $��lblSearchDateBorn��5,   k   #��[  6   $��	Date Born��6:   $��0��6N   $��2��6^   6k   (  "System.Windows.Controls.DatePicker  5-   
   -   5          Name$ txtSearchDateBorn��5-   w     DatePickerStyle#��[ 6   $
��Left=�6>   $��1��6Z   $��2��6j   $	��Top=�6�   6�   �� 5/   
   -   5        $��lblSearchDateDied��5/   k   #��[  6   $��	Date Died��6:   $��0��6N   $��3��6^   6k     50   
   -   5        $ txtSearchDateDied��50   w   #��[ 6   $
��Left=�6>   $��1��6Z   $��3��6j   $	��Top=�6�   6�   �� 52   
   -   5        $��lblSearchActivity��52   j   #��[  6   $��Activity��6:   $��2��6M   $��2��6]   6j     53   
   -	   5        $��txtSearchActivity��53   e   #��[ 6   $��3��6H   $��2��6X   6�   �� 55   
   -
   5        $��lblSearchDepth��55   g   #��[  6   $��Depth��6:   $��2��6J   $��1��6Z   6g   �� 56   
   -   5        $��txtSearchDepth��56   U     TextBoxStyle#��[ 6   $��3��68   $��1��6H    �� 
IsReadOnly . 6k   �� 57   
   -   5        $��btnSelectDepth��57   t   $	��...��6   $��4��6   $��1��6/   $��23��6<   $
��Left=�6H   $��0,2,0,0q�6c   $	��Top=�6�   $��25��6�   6�   �� 59   
   -   5        $��lbSearchlURL��59   v   #��[  6   $	��URL��6:   $��0��6H   $��4��6X   $��2��6e   6v   �� 5:   
   -   5        $��txtSearchUrl��5:   �   #��[ 6   $��1��68   $��4��6H   $��3��6U   $��2��6i   $	��615��6z   $��40��6�    .  �� TextWrapping$
 Wrap=�6�   " �� VerticalScrollBarVisibility$
 Auto=�6�   �� 5=   
   -   5        $��tbcResponse��5=   F   $��6��6   $��0��6)   $��6��69   .@�5>      �� -   5        $��
tabResults��5>   '   $��Results��6   .��5?      �  5@      �� 5A      $
��0.4* �6+   �� 5B      $��* �6+    5E      �� 5F      $��* �6(   �� 5G      $	��258 �6(   .+�5K      &   System.Windows.Controls.DataGrid  -   5        $��dtgParticipantResults��5K   �   	   AutoGenerateColumns	 .  $��0��6;   $��2��6K   $��0��6_   $��Stretch=�6l   $��Stretch=�6�   $
��Auto��6�   $
��Auto��6�   
   Columns
 5L      0  *System.Windows.Controls.DataGridTextColumn  5M      ,  &System.Windows.Controls.DataGridColumn   Header$ Index��61      Width5  /System.Windows.Controls.DataGridLengthConverter$ 50 6@   1  +System.Windows.Controls.DataGridBoundColumn   Binding 6K   �� *Index+  5N      $
 Name��61   $	 565 6?    6K   �� *Name+  5O      $ Gender��61   $ 90 6A    6M   �� *Gender+  5P      $ 	Date Born��61   $	 130 6D    6Q   �� *
DateBorn+  5Q      $ 	Date Died��61   $	 130 6D    6P   �� *
DateDied+5S      �� 5V      -   5        $��tbcAlbumInformation��5V   W     TabControlStyle#��[ 6!   $��1��6J   .@�5W      �� -   5        $��tabCollaborations��5W   :   $��Collaborations��6"   .��5X      � .+�5Y   "   �� -   5        $��lstParticipantAssociates��5Y   Y   $��Stretch=�6*   $��0,0,0,0q�6H   $��Stretch=�6y   $
��Auto��6�   $
��Auto��6�   6�   5Z      5[      5\      �� 5_      -   5        $��tbcTrackInformation��5_   x   #��[ 6!   $��1��6J   $��1��6Z   $��2,2,0,0q�6g   .@�5`      �� -   5        $��tabTrackInformation��5`   =   $��Track Appearances��6"   .��5a      � .+�5b   "     -   5        $��dtgTrackAppearances��5b   �   	 .  $��0��6G   $��2��6W   $��0��6k   $��Stretch=�6x   $��Stretch=�6�   $
��Auto��6�   $
��Auto��6�   
 5c   &     5d   *   $ Index��6=   $ 58 6L    6W   �� *Index+  5e   *   $ Track Title��6=   $	 225 6R    6^   �� *	TrackName+  5f   *   $ Album��6=   $	 195 6L    6Y   �� *	AlbumName+  5g   *   $ Activity��6=   $	 190 6O    6[   �� *ActivityName+5i   #   5j      5k      5l      5m      5n      �� 5q      -   5        $��tabResponse��5q   #   $	��XML��6   .��5r      �  5s      �� 5t      $��* �6+    5w      �� 5x      $��* �6(   .+�5{      �� -   5        $��txtResponse��5{   Y   $��1��6   $��2��6.   $��Stretch=�6;   $��Stretch=�6l    �� AcceptsReturn . $
 Auto=�6�   $ �� HorizontalScrollBarVisibility$
 Auto=�6�    . 6�   5|      5}      5~      �� 5�   
   -   5        $��	btnSearch��5�   f   $��Search��6   $��3��6"   $��4��62   $��23��6?   $
��Left=�6K   $	��Top=�6w   $��88��6�   $��
284,19,0,0q�6�   $��2��6�    �� 	IsDefault . $��3��6�   6�   �� 5�   
   $��1��6   $��5��6%   $��2��62   $��22��6C   $��Right=�6O   $��0,21,0,0q�6k   $	��Top=�6}   $	��934��6�   $��5��6�   .+�5�      /  )DecibelRestWpf.UserControls.PageNavigator  -   5          Name$ pageNavigator��5�      $��Right=�65   $	��156��6Q   5�      5�      5�      