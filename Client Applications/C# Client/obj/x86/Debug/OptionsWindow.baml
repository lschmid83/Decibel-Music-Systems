   M S B A M L   `   `   `  ����  DecibelCSharpClient"    DecibelRestWpf.OptionsWindow   5   	   U QWPFToolkit, Version=3.5.40128.1, Culture=neutral, PublicKeyToken=31bf3856ad364e35^ ZWindowsFormsIntegration, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35H 9http://schemas.microsoft.com/winfx/2006/xaml/presentation      5   	   R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5   	     � WindowStartupLocation$  CenterScreen=�5   	    � 
ResizeMode$ NoResize=�6.    � Title$ Options��5   	   $	��255��6   $	��400��6&   .��5      �  � ColumnDefinitions 5   
   �� 5      $
��Auto �6   �� 5	      $��* �6   �� 5
      $��80 �6   �� 5      $��85 �6    � RowDefinitions 5   
   �� 5      $
��Auto �6   �� 5      $
��Auto �6   �� 5      $
��Auto �6   �� 5      $
��Auto �6   �� 5      $
��Auto �6   �� 5      $
��Auto �6   �� 5      $	��18* �6    � 	Resources 5   
      ControlBaseStyle �� 
TargetType �� Property �� Value  TextBoxStyle	 �� BasedOn��%P  &	        &	 �     7   �� 5      " ��6-   .�5      ��  � ��$ 5,5,0,0q�6+   �� 5       � ��$ Center=�66   �� 5       � ��$
 Left=�68   5      �� 5      " ��6)   8	   .�5       ��  � ��$ 	10,0,15,0q�6+   �� 5!       � ��$	 260��6*   �� 5"       � ��$ 23��6+   5#      5$      .+�5&   
   �� -   5        $��label1��5&     #��[  6   $��0��6:   $��0��6J   $��4��6W   $i��cPlease enter the address of the Decibel API you would like to use 
and your application ID and Key.��6k   $��47��6�   $
��Left=�6�   $	��Top=�6  �� 5(   
   -   5        $��lblApiVersion��5(   k   #��[  6   $��API Address:��6:   $��1��6Q   $��28��6^   6k   �� 5)   
   -   5        $��txtApiAddress��5)   �   #��[  6   $��1��6<   $��1��6L   $��3��6Y   $	��250��6m   $��23��6y   $/��)http://decibel-rest-jazz.cloudapp.net/v1/��6�   6�   �� 5+   
   -   5        $��lblApplicationID��5+   n   #��[  6   $��Application ID:��6:   $��2��6T   $��28��6a   6n   �� 5,   
   -   5        $��txtApplicationID��5,   �   #��[  6   $��1��6<   $��2��6L   $��3��6Y   $	��250��6m   $��23��6y   6�   �� 5.   
   -   5        $��lblApplicationKey��5.   p   #��[  6   $��Application Key:��6:   $��3��6U   $��28��6c   6p   �� 5/   
   -   5        $��txtApplicationKey��5/   �   #��[  6   $��1��6<   $��3��6L   $��3��6Y   $	��250��6m   $��23��6y   6�   �� 51   
   -   5        $��
textBlock1��51      $��0��6   $��5��6$   $��4��61   $��23��6E   $
��Left=�6Q   $��	10,10,0,0q�6l   $	��Top=�6�   $��2��6�   �� 52      -	   5        $*��$https://developer.decibel.net/signup?�52   -   ��.  .�6r   Sign up for the Decibel API6�   53      �� 55   
   -
   5        $��btnOK��55   t   $��OK��6   $��2��6   $��6��6.   $��23��6;   $
��Left=�6G   $��0,0,0,10q�6b   $��Bottom=�6�   $��75��6�   
 �� 	IsDefault
 . 6�   �� 56   
   -   5        $��	btnCancel��56   y   $��Cancel��6   $��3��6"   $��6��62   $��23��6?   $
��Left=�6K   $��	0,0,10,10q�6f   $��Bottom=�6�   $��75��6�    �� IsCancel . 6�   57      58      