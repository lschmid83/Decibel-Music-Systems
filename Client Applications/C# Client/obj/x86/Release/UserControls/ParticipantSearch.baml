   M S B A M L   `   `   `  ���� p  lSystem.Windows.Controls.Input.Toolkit, Version=3.5.40128.1, Culture=neutral, PublicKeyToken=31bf3856ad364e35pTclr-namespace:System.Windows.Controls;assembly=System.Windows.Controls.Input.ToolkitSystem.Windows.Controls  I EDecibelRestWpf, Version=1.0.0.0, Culture=neutral, PublicKeyToken=nullI)clr-namespace:DecibelRestWpf.UserControlsDecibelRestWpf.UserControls 3   -DecibelRestWpf.UserControls.ParticipantSearch   R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35U QWPFToolkit, Version=3.5.40128.1, Culture=neutral, PublicKeyToken=31bf3856ad364e35^ ZWindowsFormsIntegration, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35H 9http://schemas.microsoft.com/winfx/2006/xaml/presentation      R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     @d2http://schemas.microsoft.com/expression/blend/2008     cmyTclr-namespace:System.Windows.Controls;assembly=System.Windows.Controls.Input.Toolkit     7c)clr-namespace:DecibelRestWpf.UserControls     .���   � 	Resources   �� MergedDictionaries �� Source��  �� $" ../Resources/DatePicker.xaml?��� $% ../Resources/SearchTabItem.xaml?�%     � ColumnDefinitions �� $��80 ��� $	��285 ��� $��70 ��� $	��285 ��� $
��Auto ���  � RowDefinitions �� $��28 ��� $��28 ��� $��28 ��� $��28 ��� $��22 ��� $��22 ��� $��* �.+��� -   $��lblSearchParameters��   ControlBaseStyle#��[  $��Search Parameters��$��0��$��0��$��4���� -   $��lblSearchName��#��[  $
��Name��$��0��$��1��-   'System.Windows.Controls.AutoCompleteBox  -   $��txtSearchName��  AutoCompleteStyle#��[ $��1��$��1��$��1q��� -   $��lblSearchDateBorn��#��[  $��	Date Born��$��0��$��2��(  "System.Windows.Controls.DatePicker  -     Name$ txtSearchDateBorn��  DatePickerStyle#��[ $
��Left=�$��1��$��2��$	��Top=��� -   $��lblSearchDateDied��#��[  $��	Date Died��$��0��$��3��  -   $ txtSearchDateDied��#��[ $
��Left=�$��1��$��3��$	��Top=��� -   $��lblSearchActivity��#��[  $��Activity��$��2��$��2��  -	   $��txtSearchActivity��#��[ $��3��$��2���� -
   $��lblSearchDepth��#��[  $��Depth��$��2��$��1���� -   $��txtSearchDepth��  TextBoxStyle#��[ $��3��$��1�� �� 
IsReadOnly . �� -   $��btnSelectDepth��$	��...��$��4��$��1��$��23��$
��Left=�$��0,2,0,0q�$	��Top=�$��25���� -   $��lbSearchlURL��#��[  $	��URL��$��0��$��4��$��2���� -   $��txtSearchUrl��#��[ $��1��$��4��$��3��$��2��$	��615��$��40�� .  �� TextWrapping$
 Wrap=�" �� VerticalScrollBarVisibility$
 Auto=��� -   $��tbcResponse��$��6��$��0��$��6��.@��� -   $��
tabResults��$��Results��.���  �� $
��0.4* ��� $��* � �� $��* ��� $	��258 �.+�&   System.Windows.Controls.DataGrid  -   $��dtgParticipantResults��	   AutoGenerateColumns	 .  $��0��$��2��$��0��$��Stretch=�$��Stretch=�$
��Auto��$
��Auto��
   Columns
 0  *System.Windows.Controls.DataGridTextColumn  ,  &System.Windows.Controls.DataGridColumn   Header$ Index��   Width5  /System.Windows.Controls.DataGridLengthConverter$ 50 1  +System.Windows.Controls.DataGridBoundColumn   Binding �� *Index+  $
 Name��$	 565  �� *Name+  $ Gender��$ 90  �� *Gender+  $ 	Date Born��$	 130  �� *
DateBorn+  $ 	Date Died��$	 130  �� *
DateDied+�� -   $��tbcAlbumInformation��  TabControlStyle#��[ $��1��.@��� -   $��tabCollaborations��$��Collaborations��.��� .+��� -   $��lstParticipantAssociates��$��Stretch=�$��0,0,0,0q�$��Stretch=�$
��Auto��$
��Auto���� -   $��tbcTrackInformation��#��[ $��1��$��1��$��2,2,0,0q�.@��� -   $��tabTrackInformation��$��Track Appearances��.��� .+�  -   $��dtgTrackAppearances��	 .  $��0��$��2��$��0��$��Stretch=�$��Stretch=�$
��Auto��$
��Auto��
   $ Index��$ 58  �� *Index+  $ Track Title��$	 225  �� *	TrackName+  $ Album��$	 195  �� *	AlbumName+  $ Activity��$	 190  �� *ActivityName+�� -   $��tabResponse��$	��XML��.���  �� $��* � �� $��* �.+��� -   $��txtResponse��$��1��$��2��$��Stretch=�$��Stretch=� �� AcceptsReturn . $
 Auto=�$ �� HorizontalScrollBarVisibility$
 Auto=� . �� -   $��	btnSearch��$��Search��$��3��$��4��$��23��$
��Left=�$	��Top=�$��88��$��
284,19,0,0q�$��2�� �� 	IsDefault . $��3���� $��1��$��5��$��2��$��22��$��Right=�$��0,21,0,0q�$	��Top=�$	��934��$��5��.+�/  )DecibelRestWpf.UserControls.PageNavigator  -     Name$ pageNavigator��$��Right=�$	��156��