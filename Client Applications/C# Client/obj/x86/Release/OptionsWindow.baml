   M S B A M L   `   `   `  ����   DecibelRestWpf"    DecibelRestWpf.OptionsWindow   R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35U QWPFToolkit, Version=3.5.40128.1, Culture=neutral, PublicKeyToken=31bf3856ad364e35^ ZWindowsFormsIntegration, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35H 9http://schemas.microsoft.com/winfx/2006/xaml/presentation      R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml       � WindowStartupLocation$  CenterScreen=� � 
ResizeMode$ NoResize=� � Title$ Options��$	��255��$	��400��.���  � ColumnDefinitions �� $
��Auto ��� $��* ��� $��80 ��� $��85 � � RowDefinitions �� $
��Auto ��� $
��Auto ��� $
��Auto ��� $
��Auto ��� $
��Auto ��� $
��Auto ��� $	��18* � � 	Resources    ControlBaseStyle �� 
TargetType �� Property �� Value  TextBoxStyle	 �� BasedOn��%�   &	        &	 Z     7   �� " ��.���  � ��$ 5,5,0,0q���  � ��$ Center=���  � ��$
 Left=��� " ��8	   .���  � ��$ 	10,0,15,0q���  � ��$	 260����  � ��$ 23��.+��� -   $��label1��#��[  $��0��$��0��$��4��$i��cPlease enter the address of the Decibel API you would like to use 
and your application ID and Key.��$��47��$
��Left=�$	��Top=��� -   $��lblApiVersion��#��[  $��API Address:��$��1��$��28���� -   $��txtApiAddress��#��[  $��1��$��1��$��3��$	��250��$��23��$/��)http://decibel-rest-jazz.cloudapp.net/v1/���� -   $��lblApplicationID��#��[  $��Application ID:��$��2��$��28���� -   $��txtApplicationID��#��[  $��1��$��2��$��3��$	��250��$��23���� -   $��lblApplicationKey��#��[  $��Application Key:��$��3��$��28���� -   $��txtApplicationKey��#��[  $��1��$��3��$��3��$	��250��$��23���� -   $��
textBlock1��$��0��$��5��$��4��$��23��$
��Left=�$��	10,10,0,0q�$	��Top=�$��2���� -	   $*��$https://developer.decibel.net/signup?���.  .�Sign up for the Decibel API�� -
   $��btnOK��$��OK��$��2��$��6��$��23��$
��Left=�$��0,0,0,10q�$��Bottom=�$��75��
 �� 	IsDefault
 . �� -   $��	btnCancel��$��Cancel��$��3��$��6��$��23��$
��Left=�$��	0,0,10,10q�$��Bottom=�$��75�� �� IsCancel . 