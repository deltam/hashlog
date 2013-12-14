# Hashlog

Declarative DSL for updating HashMap.

ハッシュマップを書き換える宣言的なDSLを書いてみた。

HashMap + Prolog(Datalog)

## Motivation

ほとんど同じだけど、微妙に違う計算ロジックみたいなのをまとめたいです。

if-elseif... みたいなコードはロジック変えるのにコードの書き換えが必要。
条件が複雑な場合はifがネストしたりしてもう大変。

こういった煩雑なコードを減らし、ロジックを更新／追加をシンプルにできるようにしたい。

## Scope

このライブラリで解決したい問題

それほど複雑じゃないけど微妙に違いのある計算ロジックがたくさんあるとする。そういうパターン化しにくい煩雑な計算ロジックを、宣言的なDSLでコードの外に置いて置けるようにする。

この問題解決のために最小限の構成を考えて作る。必要のない要素は排除（学習コストも最小限にしたい）

かなり複雑でバリエーションがほとんどない計算ロジックならコードでベタ書きしたほうが分かりやすい。
あまり複雑（定義曖昧）な計算ロジックは対象としない

Clojureで実装しているが、他の言語でも実装しやすいような仕様にする

## TODO

* DSLをJSONに変換する
* JSONからDSLへ変換する

## Usage

core.cljのcomment参照

## License

Copyright © 2013 deltam(@gmail.com)

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.