   M S B A M L   `   `   `  ����  DecibelCSharpClient&     DecibelRestWpf.TrackParticipants   5   	   U QWPFToolkit, Version=3.5.40128.1, Culture=neutral, PublicKeyToken=31bf3856ad364e35^ ZWindowsFormsIntegration, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35H 9http://schemas.microsoft.com/winfx/2006/xaml/presentation      5   	   R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5   	     � Title$  Track Participants��5   	   $	��325��6$   $	��715��61    � WindowStartupLocation$ CenterScreen=�6=    � 
ResizeMode$ NoResize=�6b   .��5      �  � ColumnDefinitions 5   
   �� 5      $��* �6    � RowDefinitions 5
   
   �� 5      $��* �6   �� 5      $��49 �6   .+�5   
   &   System.Windows.Controls.DataGrid  -   5        $��dtgTrackParticipants��5   Z      AutoGenerateColumns .  $��0��6/   $��Stretch=�6<   $��Stretch=�6v   $
��Auto��6�   $
��Auto��6�      CanUserReorderColumns .     CanUserSortColumns .     Columns 5      0  *System.Windows.Controls.DataGridTextColumn  5      ,  &System.Windows.Controls.DataGridColumn	   Header$	 Index��6%   
   Width5  /System.Windows.Controls.DataGridLengthConverter$
 55 64   1  +System.Windows.Controls.DataGridBoundColumn   Binding 6?   �� *Index+  5      $
	 Name��6%   $	
 270 63    6?   �� *Name+  5      $	 Involvement��6%   $	
 340 6:    6F   �� *Involvement+5      �� 5   
   -   5        $��btnOK��5   v   $��OK��6   $��0��6   $��1��6.   $��23��6;   $��Right=�6G   $��	0,0,10,10q�6c   $��Bottom=�6�   $��75��6�    �� 	IsDefault . 6�   5      5      