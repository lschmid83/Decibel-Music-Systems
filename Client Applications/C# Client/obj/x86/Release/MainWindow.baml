   M S B A M L   `   `   `  ���� I  EDecibelRestWpf, Version=1.0.0.0, Culture=neutral, PublicKeyToken=nullI)clr-namespace:DecibelRestWpf.UserControlsDecibelRestWpf.UserControls      DecibelRestWpf.MainWindow   R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35U QWPFToolkit, Version=3.5.40128.1, Culture=neutral, PublicKeyToken=31bf3856ad364e35^ ZWindowsFormsIntegration, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35H 9http://schemas.microsoft.com/winfx/2006/xaml/presentation      R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     8my)clr-namespace:DecibelRestWpf.UserControls     -     � 
ResizeMode$  CanResizeWithGrip=� � Title$% Decibel REST API Sample Project��$
��1024��$	��768��.���  � ColumnDefinitions �� $��* � � RowDefinitions �� $
��Auto ��� $��* ��� $��23 � � 	Resources    TabControlStyle �� 
TargetType �� Property �� Value��%�   &	        �� " ��.���  � ��$ 0,2,0,0q���  � ��$ Stretch=���  � ��$ Stretch=���  � ��$
 Auto����  � ��$
 Auto��.+�o� -   $��mnuMenu��$��23��$��0��$��0��$��Stretch=�$	��Top=�$
��Auto��.@�m� $
��File��.@�m� -   $��mnuNew��$	��New��m� -   $��	mnuSaveAs��$��
Save As...��m� -   $��mnuPrint��$��Print...��m� -   $��mnuExit��$
��Exit��m� $
��Edit��.@�m� $	��Cut�� m� Command$ ApplicationCommands.Cut��m� $
��Copy��$ ApplicationCommands.Copy��m� $��Paste��$ ApplicationCommands.Paste��m� $��Tools��.@�m� -   $��
mnuOptions��$��Options��m� $��Response Format��$
��Left=�$	��180��.@�m� -   $��mnuXmlResponse��$	��XML��	 m� IsCheckable	 . m� -	   $��mnuJsonResponse��$
��JSON��	 . m� $
��Help��.@�m� -
   $��mnuDocumentation��$��C# REST Documentation��m� -   $��mnuAbout��$��About���� -   $��tbcSearchTabs��#��[  $��0��$��1��.@��� $��Albums��.��-   'DecibelRestWpf.UserControls.AlbumSearch  -   
  Name$
 	tabAlbums���� $��Participants��.��3   -DecibelRestWpf.UserControls.ParticipantSearch  -     Name$ tabParticipants���� $��Tracks��.��-   'DecibelRestWpf.UserControls.TrackSearch  -     Name$ 	tabTracks���� $��Works��.��,   &DecibelRestWpf.UserControls.WorkSearch  -     Name$ tabWorks���� -   $��	stsStatus��$��0��$��2��$��23��$��Stretch=�$	��Top=�$
��Auto������ .R��  �� $
��Auto � �� $	��100 ��� $
��Auto �.@��� $��
0,0,-915,0q�.���� -   $��	txtStatus��$��8,0,0,0q�Ready�� $��1��.��� -   $��pgbProgress�� � Value$ 0Q�$��80��$y�Hidden=�$��12�� � IsIndeterminate . 