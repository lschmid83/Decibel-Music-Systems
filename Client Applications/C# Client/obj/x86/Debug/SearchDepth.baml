   M S B A M L   `   `   `  ����  DecibelCSharpClient     DecibelRestWpf.SearchDepth   5   	   U QWPFToolkit, Version=3.5.40128.1, Culture=neutral, PublicKeyToken=31bf3856ad364e35^ ZWindowsFormsIntegration, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35H 9http://schemas.microsoft.com/winfx/2006/xaml/presentation      5   	   R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5   	     � WindowStartupLocation$  CenterScreen=�5   	    � 
ResizeMode$ NoResize=�6.    � Title$ Search Depth��5   	   $	��328��6   $	��476��6+   .��5      �  � ColumnDefinitions 5   
   �� 5      $��* �6    � RowDefinitions 5   
   �� 5      $
��Auto �6   �� 5      $
��185* �6   �� 5      $	��49* �6   .+�5   
   �� -   5        $��label1��5   �   $?��9Please select the depth of the information to bring back.��6   $��0��6T   $��0��6d   $��28��6q   $
��Left=�6}   $��0,0,0,0q�6�   $	��Top=�6�   �� 5   
   -   5        $��lstDepth��5   _   $��1��6   $��Stretch=�6   $��5,5,5,5q�6o   $��Stretch=�6�   $
��Auto��6�   ��6=   �� *	DepthList+��5      �� 5      .R�5      ��  �� Orientation$ 
Horizontal=�6!   .+�5      �� $��chkDepth��6#   -   5        $��15��5   g   $��0,7,0,0q�6r   
 � Tag 63   �� *Index+ d� 	IsChecked 6I   �� *	Checked+6�   �� 5      $��lblDepth��6    ��60   �� *Text+6K   5      5      5      �� 5   
   -   5        $��	btnCancel��5   z   $��Cancel��6   $��0��6"   $��2��62   $��23��6?   $��Right=�6K   $��	0,0,10,10q�6g   $��Bottom=�6�   $��75��6�   6�   �� 5   
   -   5        $��btnOK��5   �   $��OK��6   $��0��6   $��2��6.   $��2��6;   $��23��6O   $��Right=�6[   $��	0,0,90,10q�6w   $��Bottom=�6�   $��75��6�   6�   5       5!      