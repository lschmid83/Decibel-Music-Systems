   M S B A M L   `   `   `  ����N  JDecibelCSharpClient, Version=1.0.0.0, Culture=neutral, PublicKeyToken=nullI)clr-namespace:DecibelRestWpf.UserControlsDecibelRestWpf.UserControls  5   	       DecibelRestWpf.MainWindow   5   	   U QWPFToolkit, Version=3.5.40128.1, Culture=neutral, PublicKeyToken=31bf3856ad364e35^ ZWindowsFormsIntegration, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35H 9http://schemas.microsoft.com/winfx/2006/xaml/presentation      5   	   R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5   	   8my)clr-namespace:DecibelRestWpf.UserControls     5   	   -   5          � 
ResizeMode$  CanResizeWithGrip=�5   	    � Title$ Decibel C# Client��5   	   $
��1024��6#   $	��768��60   .��5      �  � ColumnDefinitions 5   
   �� 5	      $��* �6    � RowDefinitions 5   
   �� 5      $
��Auto �6   �� 5      $��* �6   �� 5      $��23 �6    � 	Resources 5   
      TabControlStyle �� 
TargetType �� Property �� Value��%�   &	        �� 5      " ��6,   .�5      ��  � ��$ 0,2,0,0q�6+   �� 5       � ��$ Stretch=�68   �� 5       � ��$ Stretch=�66   �� 5       � ��$
 Auto��6*   �� 5       � ��$
 Auto��6+   5      5      .+�5   
   o� -   5        $��mnuMenu��5   V   $��23��6   $��0��6   $��0��6+   $��Stretch=�68   $	��Top=�6e   $
��Auto��6}   .@�5      m� $
��File��6   .@�5      m� -   5        $��mnuNew��5   (   $	��New��6   66   m� 5       -   5        $��	mnuSaveAs��5    /   $��
Save As...��6   6@   m� 5!      -   5        $��mnuPrint��5!   -   $��Print...��6   6=   m� 5"      -   5        $��mnuExit��5"   )   $
��Exit��6   68   5#      m� 5%      $
��Edit��6   .@�5&      m� $	��Cut��6    m� Command$ ApplicationCommands.Cut��6(   m� 5'      $
��Copy��6   $ ApplicationCommands.Copy��6*   m� 5(      $��Paste��6   $ ApplicationCommands.Paste��6*   5)      m� 5+      $��Tools��6   .@�5,      m� -   5        $��
mnuOptions��5,   ,   $��Options��6   6>   m� 5-      $��Response Format��6   $
��Left=�64   $	��180��6O   .@�5.      m� -   5        $��mnuXmlResponse��5.   ,   $	��XML��6   	 m� IsCheckable	 . 6U   m� 5/      -	   5        $��mnuJsonResponse��5/   -   $
��JSON��6   	 . 6X   50      51      m� 53      $
��Help��6   .@�54      m� -
   5        $��mnuDocumentation��54   :   $��C# REST Documentation��6   6S   m� 55      -   5        $��mnuAbout��55   *   $��About��6   6:   56      57      �� 5:   
   -   5        $��tbcSearchTabs��5:   [   #��[  6   $��0��6>   $��1��6N   .@�5;      �� $��Albums��6   .��5<      -   'DecibelRestWpf.UserControls.AlbumSearch  -   5        
  Name$
 	tabAlbums��5<   !   5=      �� 5?      $��Participants��6   .��5@      3   -DecibelRestWpf.UserControls.ParticipantSearch  -   5          Name$ tabParticipants��5@   '   5A      �� 5C      $��Tracks��6   .��5D      -   'DecibelRestWpf.UserControls.TrackSearch  -   5          Name$ 	tabTracks��5D   !   5E      �� 5G      $��Works��6   .��5H      ,   &DecibelRestWpf.UserControls.WorkSearch  -   5          Name$ tabWorks��5H       5I      5J      �� 5M   
   -   5        $��	stsStatus��5M   [   $��0��6   $��2��6$   $��23��61   $��Stretch=�6=   $	��Top=�6l   $
��Auto��6�   ��5N      �� 5O      .R�5P      �  5Q      �� 5R      $
��Auto �6,    5U      �� 5V      $	��100 �6/   �� 5W      $
��Auto �6/   5Y      5Z      .@�5]      �� $��
0,0,-915,0q�6   .��5^      �� -   5        $��	txtStatus��5^      $��8,0,0,0q�6-   Ready6E   5_      �� 5a      $��1��6   .��5b      � -   5        $��pgbProgress��5b   G    � Value$ 0Q�6   $��80��6(   $y�Hidden=�63   $��12��6Z    � IsIndeterminate . 6f   5c      5d      5e      5f      