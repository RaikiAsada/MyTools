# frozen_string_literal: true
require './dom/select_brunch_build.rb'
class BuildBrunchHistory
  def self.load(job)
    groups = SelectBrunchBuild.load(job)
                              .sort_by{|build| build.number}
                              .reject{|build| build.get_brunch.nil?}
                              .group_by{|build| build.get_brunch}
    return BuildBrunchHistory.new(groups)
  end
  def initialize(build_map_brunch)
    @build_map_brunch = build_map_brunch
  end
  def build_brunches
    return @build_map_brunch.keys.sort_by{|brunch| brunch}
  end
  def all_builds(brunch)
    return @build_map_brunch[brunch].sort_by{|build| build.number}
  end
  def last_build(brunch)
    return all_builds(brunch).last
  end
end
