# Hashlog

ハッシュマップを書き換える宣言的なDSLを書いてみた。
HashMap + Prolog(Datalog)

## Motivation

ほとんど同じだけど、微妙に違う計算ロジックみたいなのをまとめたいです。
if-elseif... みたいなコードはロジック変えるのにコードの書き換えが必要。
条件が複雑な場合はifがネストしたりしてもう大変。。。

宣言的なコード、データフロー図みたいな感じにしたい

## Scope

このライブラリで解決したい問題

それほど複雑じゃないけど微妙に違いのある計算ロジックがたくさんある。
煩雑な計算ロジックを宣言的なDSLでコードの外に置いて置けるようにする。
（設定ファイル化といったほうが分かりやすいか）

この問題解決のために最小限の構成を考えて作る。必要のない要素は排除（学習コストも最小限にしたい）

かなり複雑でバリエーションがほとんどない計算ロジックならコードでベタ書きしたほうが分かりやすい。
あまり複雑（定義曖昧）な計算は対象としない

## TODO

DSLのJSON変換
JSONからDSLへの変換

## Usage

FIXME

## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
